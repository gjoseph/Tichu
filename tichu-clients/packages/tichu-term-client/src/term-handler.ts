import {
  ActivityMessage,
  Card,
  cardFromName,
  ErrorMessage,
  IncomingChatMessage,
  IncomingHandMessage,
  IncomingPlayerPlaysResponse,
  JoinParam,
  NewTrickParam,
  OutgoingChatMessage,
  OutgoingGameMessage,
  OutgoingMessage,
  PlayerIsReadyParam,
  PlayerPlaysParam,
  SendFunction,
  TichuWebSocketHandler,
  TichuWebSocketHandlerFactory,
} from "tichu-client-ts-lib";
import { Log } from "./log";
import PromptUI from "inquirer/lib/ui/prompt";
import inquirer from "inquirer";

export const newTerminalHandler: TichuWebSocketHandlerFactory = (
  send: SendFunction
) => {
  return new TerminalHandler(send, new Log());
};

class TerminalHandler implements TichuWebSocketHandler {
  private nextPrompt: (() => Promise<OutgoingMessage>) | undefined;
  private currentPromptUi: PromptUI | undefined;

  constructor(readonly send: SendFunction, readonly log: Log) {}

  // ==== Websocket callbacks
  onConnect = () => {
    this.log.control("Connected (press CTRL+C to quit)");
    // TODO nextPrompt=join if game already exists in room, otherwise setup game
    this.nextPrompt = this.promptForJoin;
  };

  onConnectionClose = (code: number, reason: string) => {
    this.log.control(`Disconnected (code: ${code}, reason: "${reason}")`);
    process.exit();
  };

  onWebsocketError = (err: Error) => {
    this.log.error("Websocket error: " + err.message);
    process.exit(-1);
  };

  onPing = () => {
    this.log.control("Received ping");
  };

  onPong = () => {
    this.log.control("Received pong");
  };

  // ==== Message handling

  beforeMessageProcessing() {
    this.closeCurrentPrompt();
  }

  afterMessageProcessing() {
    if (this.nextPrompt) {
      this.nextPrompt().then(this.send);
    }
  }

  handleChatMessage(msg: IncomingChatMessage) {
    this.log.chat(`${msg.from}: ${msg.content}`);
  }

  handleActivityMessage(msg: ActivityMessage) {
    this.log.activity(`${msg.actor} ${msg.activity}`);
  }

  handleErrorMessage(msg: ErrorMessage) {
    this.log.error(
      `Error caused by ${msg.actor} - contact us with this reference: ${msg.traceId} (txId: ${msg.txId})`
    );
  }

  handleHandMessage(msg: IncomingHandMessage) {
    // msg has a txId but we don't really care while "fetching hand" isn't its own action
    // for the originating client, the txId will have been removed from queue with the game message
    // for the other 3 clients, it will be unknown
    this.log.debug("Received cards: " + msg.hand.cards);
    // TODO typing should be in IncomingHandMessage, if we bothered copying the object props into instance rather than cast json
    const cards: Card[] = msg.hand.cards.map(cardFromName);
    this.nextPrompt = this.promptForCards(cards);
  }

  // ==== Game message visitors
  handleJoin = (isResponse: boolean) => {
    return {
      "can-not-join-full-table": () => {
        this.log.activity("Sorry, this table is full");
        this.nextPrompt = undefined;
      },
      ok: () => {
        if (isResponse) {
          this.log.activity("Waiting for others");
          this.nextPrompt = undefined;
        }
      },
      "ok-table-is-now-full": () => {
        this.nextPrompt = this.promptForReadiness;
      },
    };
  };

  handleReady = (isResponse: boolean) => {
    return {
      ok: () => {
        if (isResponse) {
          this.debug("Waiting for others to be ready");
          this.nextPrompt = undefined;
        }
      },
      "ok-started": () => {
        this.debug("Let's get started!");
        // expecting each player to get a hand message now ...
        // see handleHandMessage
        this.nextPrompt = undefined;
      },
    };
  };

  handlePlayResult = (
    isResponse: boolean,
    msg: IncomingPlayerPlaysResponse
  ) => {
    return {
      "next-player-goes": () => {
        this.log.activity(`It's now ${msg.nextPlayer}'s turn`);
      },
      "trick-end": () => {
        // TODO Anyone will be table to trigger new-trick here, but maybe this should only be for the winning player?
        // (I don't think the info is currently in the messages)
        this.nextPrompt = this.promptForNewTrick;
      },
      // errors:
      "invalid-play": this.logErrorNoPromptChange(msg.message),
      "invalid-state": this.logErrorNoPromptChange(msg.message),
      "not-in-hand": this.logErrorNoPromptChange(msg.message),
      "too-weak": this.logErrorNoPromptChange(msg.message),
    };
  };

  // ==== Prompts

  private ask = <T>(question: any): Promise<T> => {
    // question: DistinctQuestion) => {
    this.closeCurrentPrompt();
    const promptPromiseAndUi = inquirer.prompt([question]);
    // inquirer.prompt() actuall returns Promise<T> & {ui: PromptUI} so we keep track of it to be able to close it
    this.currentPromptUi = promptPromiseAndUi.ui;
    return promptPromiseAndUi as Promise<T>;
  };

  private closeCurrentPrompt() {
    // can't use ?. operator below because of the @ts-ignore statement ...
    if (this.currentPromptUi) {
      // @ts-ignore : close() is protected but we really want to call it ...
      this.currentPromptUi.close();
    }
  }

  private promptForJoin = () => {
    return this.ask({
      type: "list",
      name: "teamId",
      choices: [
        { name: "No, just observe", value: -1 },
        { name: "Team #1", value: 0 },
        { name: "Team #2", value: 1 },
      ],
      message: "Join a team?",
    }).then((answers: any) => {
      if (answers.teamId >= 0) {
        return new OutgoingGameMessage(new JoinParam(answers.teamId));
      } else {
        return new OutgoingChatMessage("Not joining");
      }
    });
  };

  private promptForReadiness = () => {
    return this.ask({
      type: "confirm",
      name: "ready",
      message: "Are you ready?",
    }).then((answers: any) => {
      if (answers.ready) {
        return new OutgoingGameMessage(new PlayerIsReadyParam());
      } else {
        return new OutgoingChatMessage("Not ready");
      }
    });
  };

  private promptForNewTrick = () => {
    return this.ask({
      type: "confirm",
      name: "ready",
      message: "Kick-off new trick?",
    }).then((answers: any) => {
      if (answers.ready) {
        return new OutgoingGameMessage(new NewTrickParam());
      } else {
        return new OutgoingChatMessage("Not ready for a new trick");
      }
    });
  };

  private promptForCards = (cards: Card[]) => () => {
    const choices = cards.map((c) => {
      return { value: c.shortName, name: c.name };
    });
    const question = {
      message: "Pick cards", // with space, hit enter to play them. Just hit enter to pass.",
      type: "checkbox",
      name: "cards",
      choices: choices,
      pageSize: 20,
    };
    return this.ask(question).then((answers: any) => {
      return new OutgoingGameMessage(new PlayerPlaysParam(answers.cards));
    });
  };

  private logErrorNoPromptChange = (s: string) => () => {
    this.log.error(`Yeah nah ${s}`);
  };

  debug = (...msg: any) => {
    const msgStr = msg
      .map((m: any) => {
        if (typeof m !== "string") {
          return JSON.stringify(m, null, 2);
        } else {
          return m;
        }
      })
      .join(" ");
    this.log.debug(msgStr);
  };
}
