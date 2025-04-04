import {
  ActivityMessage,
  Card,
  cardFromName,
  ErrorMessage,
  IncomingChatMessage,
  IncomingGameStatusMessage,
  IncomingHandMessage,
  IncomingPlayerPlaysResponse,
  TichuWebSocketHandler,
  TichuWebSocketHandlerFactory,
} from "tichu-client-ts-lib";
import { ActivityLogMessage } from "./components/ActivityLog";
import { Log } from "./Log";
import { GameState } from "./model/GameState";
import { RoomState, RoomStatus } from "./model/RoomState";
import { Notifier } from "./notifications";

// Callbacks given by Room/Game
type SetRoomState = (fn: (oldState: RoomState) => RoomState) => void; //SetRoomStateNewOnly & SetRoomStateOldToNew;
type SetGameState = (fn: (oldState: GameState) => GameState) => void;
type NewChatMessage = (newMessage: IncomingChatMessage) => void;
type NewActivityLog = (newMessage: ActivityLogMessage) => void;

export const newReactHandler: (
  notify: Notifier,
  setRoomState: SetRoomState,
  setGameState: SetGameState,
  newChatMessage: NewChatMessage,
  newActivityLog: NewActivityLog,
) => TichuWebSocketHandlerFactory = (
  notify,
  setRoomState,
  setGameState,
  newChatMessage,
  newActivityLog,
) => {
  return () => {
    return new ReactAppHandler(
      notify,
      setRoomState,
      setGameState,
      newChatMessage,
      newActivityLog,
      new Log(true, newActivityLog),
    );
  };
};

class ReactAppHandler implements TichuWebSocketHandler {
  constructor(
    readonly notify: Notifier,
    readonly setRoomState: SetRoomState,
    readonly setGameState: SetGameState,
    readonly newChatMessage: NewChatMessage,
    readonly newActivityLog: NewActivityLog,
    readonly log: Log,
  ) {}

  // ==== Websocket callbacks
  onConnect = () => {
    this.log.debug("Connected (press CTRL+C to quit)");
    this.setRoomState(() => new RoomState(RoomStatus.CONNECTED));
    this.notify("Connected !", {
      severity: "info",
    });
  };

  onConnectionClose = (code: number, reason: string, wasClean: boolean) => {
    this.log.debug(
      `Disconnected (code: ${code}, reason: "${reason}", ${
        wasClean ? "clean" : "dirty"
      })`,
    );
    this.notify("Disconnected", {
      severity: "warning",
    });
  };

  onWebsocketError = (msg?: string, err?: Error) => {
    // no message or error object were passed for browser websockets at the time of writing; logging them anyway in case that changes
    this.log.debug("Websocket error", msg, err);
  };

  onPing = () => {
    this.log.debug("Received ping");
  };

  onPong = () => {
    this.log.debug("Received pong");
  };

  // ==== Message handling

  beforeMessageProcessing() {}

  afterMessageProcessing() {}

  handleChatMessage(msg: IncomingChatMessage) {
    this.log.debug(`Chat: ${msg.from}: ${msg.content}`);
    this.newChatMessage(msg);
  }

  handleActivityMessage(msg: ActivityMessage) {
    this.log.debug(`Activity: ${msg.actor} ${msg.activity}`);
    this.newActivityLog({
      debug: false,
      message: `${msg.actor} ${msg.activity}'d`,
    });
  }

  handleErrorMessage(msg: ErrorMessage) {
    this.log.error(
      `Error caused by ${msg.actor} - contact us with this reference: ${msg.traceId} (txId: ${msg.txId})`,
    );
  }

  handleStatusMessage(msg: IncomingGameStatusMessage) {
    this.log.debug("Received status", JSON.stringify(msg));

    // TODO .filter() me out
    this.setGameState(
      (oldState: GameState) =>
        new GameState(msg.currentPlayer, oldState.hand, msg.players),
    );
  }

  handleHandMessage(msg: IncomingHandMessage) {
    // TODO the below is copied from term-handler.ts and I have no memory what it means
    // msg has a txId but we don't really care while "fetching hand" isn't its own action
    // for the originating client, the txId will have been removed from queue with the game message
    // for the other 3 clients, it will be unknown
    this.log.debug("Received cards", msg.hand.cards);
    //todo what// TODO typing should be in IncomingHandMessage, if we bothered copying the object props into instance rather than cast json
    const cards: Card[] = msg.hand.cards.map(cardFromName);

    // TODO this feels completely wrong, this shouldn't set the whole state
    // as it is, a library like immerjs would help, but let's first try to extract state elsewhere instead
    this.setGameState(
      (oldState: GameState) =>
        new GameState(oldState.currentPlayer, cards, oldState.otherPlayers),
    );
  }

  // ==== Game message visitors
  handleJoin = (isResponse: boolean) => {
    return {
      "can-not-join-full-table": () => {
        this.debug("Nah this table is full");
        // TODO
      },
      ok: () => {
        if (isResponse) {
          this.debug("Waiting for others");
          this.notify("Welcome");
          this.setRoomState(() => new RoomState(RoomStatus.NEED_PLAYERS));
        }
      },
      "ok-table-is-now-full": () => {
        // this hinges on the fact we receive messages for other joiners
        this.setRoomState(() => new RoomState(RoomStatus.WAIT_FOR_READY));
      },
    };
  };

  handleReady = (isResponse: boolean) => {
    return {
      ok: () => {
        if (isResponse) {
          this.debug("Waiting for others to be ready");
        }
      },
      // TODO Whats the difference between these 2 again
      "ok-started": () => {
        this.debug("Let's get started!");
        // expecting each player to get a hand message now ...
        // see handleHandMessage
        this.setRoomState(() => new RoomState(RoomStatus.PLAYING));
      },
    };
  };

  handlePlayResult = (
    isResponse: boolean,
    msg: IncomingPlayerPlaysResponse,
  ) => {
    return {
      "next-player-goes": () => {
        this.log.debug(
          `${msg.actor} played, ${JSON.stringify(msg)}. It's now ${
            msg.nextPlayer
          }'s turn`,
        );
        this.setGameState(
          (oldState: GameState) =>
            new GameState(msg.nextPlayer, oldState.hand, oldState.otherPlayers),
        );
      },
      "trick-end": () => {
        // TODO Anyone will be table to trigger new-trick here, but maybe this should only be for the winning player?
        // (I don't think the info is currently in the messages)
      },
      // errors:
      "invalid-play": this.logErrorNoPromptChange(msg.message),
      "invalid-state": this.logErrorNoPromptChange(msg.message),
      "not-in-hand": this.logErrorNoPromptChange(msg.message),
      "too-weak": this.logErrorNoPromptChange(msg.message),
    };
  };

  private logErrorNoPromptChange = (s: string) => () => {
    this.log.error("Yeah nah", s);
  };

  debug = (...msg: unknown[]) => {
    this.log.debug(...msg);
  };
}
