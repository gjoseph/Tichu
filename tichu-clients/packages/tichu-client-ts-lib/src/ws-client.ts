import wsWebSocket from "ws";
import {
  ActivityMessage,
  ErrorMessage,
  IncomingChatMessage,
  IncomingGameMessage,
  IncomingHandMessage,
  IncomingMessage,
  IncomingPlayerPlaysResponse,
  JoinResult,
  OutgoingMessage,
  PlayerIsReadyResult,
  PlayResult,
} from "./messages";
import { visitEnumValue } from "ts-enum-util";
import {
  TichuWebSocketHandler,
  TichuWebSocketHandlerFactory,
} from "./ws-handler";

export type SendFunction = (msg: OutgoingMessage) => void;
export type WebSocketTypes = WebSocket | wsWebSocket;

// Events used in websocket listeners are the common properties of browser websocket and the ws package's corresponding events,
// here's some type definitions for them:
type OnOpenParams = {};
type OnCloseParams = {
  code: number;
  reason: string;
  wasClean: boolean;
};
// browser Websocket does not have an ErrorEvent type; it's generic Event with type "error", no message or error object.
type OnErrorParams = { message?: string; error?: any };
// browser Websocket as `lastEventId` and `origin` properties in addition for MessageEvent, not sure what they're for
type OnMessageParams = { data: WebSocketData };
// ws supports string | Buffer | ArrayBuffer | Buffer[] -- not sure if/how to deal with binary messages, and if that's useful
type WebSocketData = string;

export class WSTichuClient {
  private readonly handler: TichuWebSocketHandler;
  private webSocket: WebSocketTypes | undefined;

  /**
   * Message IDs we sent and expect a response about.
   * TODO: for some reason, we only track these for game messages - should we track
   * for all?
   */
  private readonly waitingForAnswer: string[];

  constructor(
    readonly bogusCredentials: string, // TODO
    handlerFactory: TichuWebSocketHandlerFactory
  ) {
    this.handler = handlerFactory(this.send);
    this.waitingForAnswer = [];
  }

  connect(url: string) {
    this.webSocket = this.webSocketSetup(url);
    return this;
  }

  // After connection, only actions should be play or pass
  // and its maybe just a single question that lists cards and a special key/option to pass
  // .. or just, if no card selected > pass
  // and a confirmation
  //
  // https://www.npmjs.com/package/read has a timeout function which could also be interesting
  // https://www.npmjs.com/package/https-proxy-agent could be needed as well

  send = (msg: OutgoingMessage) => {
    this.waitingForAnswer.push(msg.txId); // Do we care for chat messages?
    const msgJson = JSON.stringify(msg);
    this.debug(" Sending", msgJson);
    this.ws().send(msgJson);
  };

  close = () => {
    this.ws().close();
  };

  private receive = (data: WebSocketData) => {
    this.handler.beforeMessageProcessing();

    // there's gotta be a better way than just casting to string
    const msg = JSON.parse(data as string) as IncomingMessage;
    this.debug("Received", msg);

    // Game messages are handled more specifically here before being
    // delegated to handler - other simpler messages are delegated straightaway.
    if (msg.messageType === "game") {
      this.handleGameMessage(msg as IncomingGameMessage);
    } else if (msg.messageType === "hand") {
      this.handler.handleHandMessage(msg as IncomingHandMessage);
    } else if (msg.messageType === "chat") {
      this.handler.handleChatMessage(msg as IncomingChatMessage);
    } else if (msg.messageType === "activity") {
      this.handler.handleActivityMessage(msg as ActivityMessage);
    } else if (msg.messageType === "error") {
      this.handler.handleErrorMessage(msg as ErrorMessage);
    } else {
      throw new Error("Unknown message type: " + msg.messageType);
    }

    this.handler.afterMessageProcessing();
  };

  private handleGameMessage(msg: IncomingGameMessage) {
    // do we care for all messages ?
    const idxCorrespondingRequest = this.waitingForAnswer.indexOf(msg.txId);
    const isResponse = idxCorrespondingRequest >= 0;
    if (isResponse) {
      this.debug(
        `Removing ${msg.txId} from message queue - remaining:`,
        this.waitingForAnswer
      );
      // TODO add test for this
      this.waitingForAnswer.splice(idxCorrespondingRequest, 1);
    }

    switch (msg.forAction) {
      case "init":
        break;
      case "join":
        visitEnumValue(msg.result as JoinResult).with(
          this.handler.handleJoin(isResponse)
        );
        break;
      case "ready":
        visitEnumValue(msg.result as PlayerIsReadyResult).with(
          this.handler.handleReady(isResponse)
        );
        break;
      case "new-trick":
        this.debug("What do we do here?"); // TODO
        break;
      case "play":
        const msg1 = msg as IncomingPlayerPlaysResponse;
        visitEnumValue(msg1.result as PlayResult).with(
          this.handler.handlePlayResult(isResponse, msg1)
        );
        break;
      default:
        throw new Error("Unknown action: " + msg.forAction);
    }
  }

  private ws(): WebSocketTypes {
    if (!this.webSocket) {
      throw new Error("Websocket is not setup!");
    }
    return this.webSocket;
  }

  private webSocketSetup(url: string): WebSocketTypes {
    // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
    const userPass = `${this.bogusCredentials}:${this.bogusCredentials}`;
    // options don't exist in the browser js API for websocket, so we can't pass a header
    // doing this for now -- will need to use proper tokens/tickets
    url = url.replace("://", `://${userPass}@`);

    let ws: WebSocketTypes;
    if (typeof WebSocket !== "undefined") {
      ws = new WebSocket(url);
    } else {
      ws = new wsWebSocket(url);
    }
    // TODO wtf are subprotocols

    ws.onopen = (event: OnOpenParams) => this.handler.onConnect;
    ws.onclose = (event: OnCloseParams) => {
      this.handler.onConnectionClose(event.code, event.reason, event.wasClean);
    };
    ws.onerror = (event: OnErrorParams) => {
      this.debug(event.message);
      this.handler.onWebsocketError(event.message, event.error);
    };

    ws.onmessage = (event: OnMessageParams) => {
      this.receive(event.data);
    };

    // TODO fix this?
    // ws.on("ping", this.handler.onPing);
    // ws.on("pong", this.handler.onPong);

    return ws;
  }

  private debug = (...msg: any) => {
    this.handler.debug(msg);
  };
}
