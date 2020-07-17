import {
  ActivityMessage,
  Card,
  cardFromName,
  ErrorMessage,
  IncomingChatMessage,
  IncomingHandMessage,
  IncomingPlayerPlaysResponse,
  TichuWebSocketHandler,
  TichuWebSocketHandlerFactory,
} from "tichu-client-ts-lib";
import { OptionsObject, SnackbarKey, SnackbarMessage } from "notistack";
import { Log } from "./Log";
import { GameState } from "./model/GameState";
import { RoomState, RoomStatus } from "./model/RoomState";
import { ActivityLogMessage } from "./components/ActivityLog";

// Callbacks given by Room/Game
type SetRoomState = (roomState: RoomState) => void;
type SetGameState = (gameState: GameState) => void;
type NewChatMessage = (newMessage: IncomingChatMessage) => void;
type NewActivityLog = (newMessage: ActivityLogMessage) => void;

type Notifier = (
  message: SnackbarMessage,
  options?: OptionsObject
) => SnackbarKey;

export const newReactHandler: (
  notify: Notifier,
  setRoomState: SetRoomState,
  setGameState: SetGameState,
  newChatMessage: NewChatMessage,
  newActivityLog: NewActivityLog
) => TichuWebSocketHandlerFactory = (
  notify,
  setRoomState,
  setGameState,
  newChatMessage,
  newActivityLog
) => {
  return () => {
    return new ReactAppHandler(
      notify,
      setRoomState,
      setGameState,
      newChatMessage,
      newActivityLog,
      new Log(true, newActivityLog)
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
    readonly log: Log
  ) {}

  // ==== Websocket callbacks
  onConnect = () => {
    this.log.debug("Connected (press CTRL+C to quit)");
    this.setRoomState(new RoomState(RoomStatus.CONNECTED));
    this.notify("Connected !");
  };

  onConnectionClose = (code: number, reason: string, wasClean: boolean) => {
    this.log.debug(
      `Disconnected (code: ${code}, reason: "${reason}", ${
        wasClean ? "clean" : "dirty"
      })`
    );
    this.notify("Disconnected");
  };

  onWebsocketError = (msg?: string, err?: Error) => {
    // no message or error object for browser websockets
    this.log.debug("Websocket error");
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
      `Error caused by ${msg.actor} - contact us with this reference: ${msg.traceId} (txId: ${msg.txId})`
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

    // TODO this is completely wrong, this shouldn't set the whole state
    this.setGameState(new GameState(cards));
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
          this.setRoomState(new RoomState(RoomStatus.NEED_PLAYERS));
        }
      },
      "ok-table-is-now-full": () => {
        // this hinges on the fact we receive messages for other joiners
        this.setRoomState(new RoomState(RoomStatus.WAIT_FOR_READY));
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
        this.setRoomState(new RoomState(RoomStatus.PLAYING));
      },
    };
  };

  handlePlayResult = (
    isResponse: boolean,
    msg: IncomingPlayerPlaysResponse
  ) => {
    return {
      "next-player-goes": () => {
        this.log.debug(`It's now ${msg.nextPlayer}'s turn`);
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
    this.log.error(`Yeah nah ${s}`);
  };

  debug = this.log.debug;
}
