import WebSocket from "isomorphic-ws";
import inquirer from "inquirer";
import { Card, cardFromName } from "tichu-client-ts-lib";
import {
  ActivityMessage,
  ErrorMessage,
  IncomingChatMessage,
  IncomingGameMessage,
  IncomingHandMessage,
  IncomingMessage,
  JoinResult,
  JoinParam,
  OutgoingChatMessage,
  OutgoingGameMessage,
  OutgoingMessage,
  PlayerIsReadyParam,
  PlayerIsReadyResult,
  PlayerPlaysParam,
  PlayResult,
  NewTrickParam,
  IncomingPlayerPlaysResponse,
} from "tichu-client-ts-lib";
import { GameOpts } from "./startup";
import { Console } from "./console";
import PromptUI from "inquirer/lib/ui/prompt";
import { visitEnumValue } from "ts-enum-util";

export class WSTichuClient {
  private readonly console: Console;
  private webSocket: WebSocket | undefined;
  private nextPrompt: (() => Promise<OutgoingMessage>) | undefined;
  private currentPromptUi: PromptUI | undefined;
  private waitingForAnswer: string[];

  constructor(readonly opts: GameOpts) {
    this.console = new Console();
    this.nextPrompt = undefined;
    this.currentPromptUi = undefined;
    this.waitingForAnswer = [];
  }

  connect(url: string) {
    this.webSocket = this.webSocketSetup(url, this.console);
    return this;
  }

  // After connection, only actions should be play or pass
  // and its maybe just a single question that lists cards and a special key/option to pass
  // .. or just, if no card selected > pass
  // and a confirmation
  //
  // https://www.npmjs.com/package/read has a timeout function which could also be interesting
  // https://www.npmjs.com/package/https-proxy-agent could be needed as well

  waitUntilDone(): Status {
    // while (true) {
    //     console.log("...next question...");
    //     this.prompt()
    //     console.log("....thanks.,,,")
    // }
    return "Done";
  }

  ask = <T>(question: any): Promise<T> => {
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

  promptForJoin = () => {
    return this.ask({
      type: "confirm",
      name: "join",
      message: "Join table?",
    }).then((answers: any) => {
      if (answers.join) {
        return new OutgoingGameMessage(new JoinParam(this.opts.team));
      } else {
        return new OutgoingChatMessage("Not joining");
      }
    });
  };

  promptForReadiness = () => {
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

  promptForNewTrick = () => {
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

  promptForCards = (cards: Card[]) => () => {
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

  private onConnect() {
    // TODO nextPrompt=join if game already exists in room, otherwise setup game
    this.nextPrompt = this.promptForJoin;
  }

  send = (msg: OutgoingMessage) => {
    this.waitingForAnswer.push(msg.txId); // Do we care for chat messages?
    const msgJson = JSON.stringify(msg);
    this.console.debug(" Sending", msgJson);
    this.ws().send(msgJson);
  };

  private receive(data: WebSocket.Data) {
    this.closeCurrentPrompt();

    // there's gotta be a better way than just casting to string
    const msg = JSON.parse(data as string) as IncomingMessage;
    this.console.debug("Received", msg);

    if (msg.messageType === "chat") {
      this.handleChatMessage(msg as IncomingChatMessage);
    } else if (msg.messageType === "game") {
      this.handleGameMessage(msg as IncomingGameMessage);
    } else if (msg.messageType === "hand") {
      this.handleHandMessage(msg as IncomingHandMessage);
    } else if (msg.messageType === "activity") {
      this.handleActivityMessage(msg as ActivityMessage);
    } else if (msg.messageType === "error") {
      this.handleErrorMessage(msg as ErrorMessage);
    } else {
      throw new Error("Unknown message type: " + msg.messageType);
    }

    if (this.nextPrompt) {
      this.nextPrompt().then(this.send);
    }
  }

  private handleChatMessage(msg: IncomingChatMessage) {
    this.console.print(
      Console.Types.Incoming,
      `${msg.from}: ${msg.content}`,
      Console.Colors.Blue
    );
  }

  private handleErrorMessage(msg: ErrorMessage) {
    this.console.print(
      Console.Types.Incoming,
      `Error caused by ${msg.actor} - contact us with this reference: ${msg.traceId} (txId: ${msg.txId})`,
      Console.Colors.Red
    );
  }

  private handleActivityMessage(msg: ActivityMessage) {
    this.console.print(
      Console.Types.Incoming,
      `${msg.actor} ${msg.activity}`,
      Console.Colors.Green
    );
  }

  private handleGameMessage(msg: IncomingGameMessage) {
    // do we care for all messages ?
    const idxCorrespondingRequest = this.waitingForAnswer.indexOf(msg.txId);
    const isResponse = idxCorrespondingRequest >= 0;
    if (isResponse) {
      console.log(
        "Removing message",
        msg.txId,
        "from queue ",
        this.waitingForAnswer
      );
      this.waitingForAnswer.splice(idxCorrespondingRequest);
    }

    // set nextPrompt depending on received message
    // or leave it as-is
    // receive() will reapply nextPrompt
    switch (msg.forAction) {
      case "init":
        break;
      case "join":
        visitEnumValue(msg.result as JoinResult).with({
          "can-not-join-full-table": () => {
            this.console.debug("Nah this table is full");
            this.nextPrompt = undefined;
          },
          ok: () => {
            if (isResponse) {
              this.console.debug("Waiting for others");
              this.nextPrompt = undefined;
            }
          },
          "ok-table-is-now-full": () => {
            this.nextPrompt = this.promptForReadiness;
          },
        });
        break;
      case "ready":
        visitEnumValue(msg.result as PlayerIsReadyResult).with({
          ok: () => {
            if (isResponse) {
              this.console.debug("Waiting for others to be ready");
              this.nextPrompt = undefined;
            }
          },
          "ok-started": () => {
            this.console.debug("Let's get started!");
            // expecting each player to get a hand message now ...
            // see handleHandMessage
            this.nextPrompt = undefined;
          },
        });
        break;
      case "new-trick":
        this.console.debug("What do we do here?");
        break;
      case "play":
        const msg1 = msg as IncomingPlayerPlaysResponse;
        visitEnumValue(msg1.result as PlayResult).with({
          "next-player-goes": () => {
            this.console.print(
              Console.Types.Control,
              `It's now ${msg1.nextPlayer}'s turn`,
              Console.Colors.Green
            );
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
        });
        break;
      default:
        throw new Error("Unknown action: " + msg.forAction);
    }

    // TODO etc...
  }

  private handleHandMessage(msg: IncomingHandMessage) {
    // msg has a txId but we don't really care while "fetching hand" isn't its own action
    // for the originating client, the txId will have been removed from queue with the game message
    // for the other 3 clients, it will be unknown
    // this.console.debug("Received cards", msg.hand.cards);
    // TODO typing should be in IncomingHandMessage, if we bothered copying the object props into instance rather than cast json
    const cards: Card[] = msg.hand.cards.map(cardFromName);
    this.nextPrompt = this.promptForCards(cards);
  }

  private logErrorNoPromptChange = (s: string) => () => {
    this.console.print(
      Console.Types.Error,
      `Yeah nah ${s}`,
      Console.Colors.Red
    );
  };

  close = () => {
    this.ws().close();
  };

  private ws(): WebSocket {
    if (!this.webSocket) {
      throw new Error("Websocket is not setup!");
    }
    return this.webSocket;
  }

  private webSocketSetup(url: string, wsConsole: Console): WebSocket {
    const noop = () => {};

    // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
    const userPass = `${this.opts.user}:${this.opts.user}`;
    const authHeaderValue = "Basic " + Buffer.from(userPass).toString("base64");
    const ws = new WebSocket(url, {
      headers: {
        Authorization: authHeaderValue,
      },
    }); // TODO wtf are subprotocols

    ws.on("open", () => {
      wsConsole.print(
        Console.Types.Control,
        "Connected (press CTRL+C to quit)",
        Console.Colors.Green
      );
      this.onConnect();
    });

    ws.on("close", (code, reason) => {
      wsConsole.print(
        Console.Types.Control,
        `Disconnected (code: ${code}, reason: "${reason}")`,
        Console.Colors.Green
      );
      process.exit();
    });

    ws.on("error", (err) => {
      wsConsole.print(Console.Types.Error, err.message, Console.Colors.Yellow);
      process.exit(-1);
    });

    ws.on("message", (data: WebSocket.Data) => {
      this.receive(data);
    });

    ws.on("ping", () => {
      wsConsole.print(
        Console.Types.Incoming,
        "Received ping",
        Console.Colors.Blue
      );
    });

    ws.on("pong", () => {
      wsConsole.print(
        Console.Types.Incoming,
        "Received pong",
        Console.Colors.Blue
      );
    });

    return ws;
  }
}

export type Status = "Done" | "Not done";
