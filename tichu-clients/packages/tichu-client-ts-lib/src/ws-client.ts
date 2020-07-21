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
type OnReceiveParams = { data: WebSocketData };
// ws supports string | Buffer | ArrayBuffer | Buffer[] -- not sure if/how to deal with binary messages, and if that's useful
type WebSocketData = string;

/**
 * A function to callback when receiving a response to a particular message.
 */
export type OnResponse = () => void;

const NOOP = () => {};

type OnOpen = (event: OnOpenParams) => void;
type OnClose = (event: OnCloseParams) => void;
type OnError = (event: OnErrorParams) => void;
type OnReceive = (event: OnReceiveParams) => void;
type OnSend = (msg: OutgoingMessage) => void;

export class WSTichuClient {
  private readonly handler: TichuWebSocketHandler;
  private webSocket: WebSocketTypes | undefined;

  // event handlers
  private onOpens: OnOpen[];
  private onCloses: OnClose[];
  private onErrors: OnError[];
  private onReceives: OnReceive[];
  private onSends: OnSend[];

  /**
   * Message IDs we sent and expect a response about.
   */
  private readonly waitingForResponse: Map<string, OnResponse>;

  constructor(
    readonly bogusCredentials: string, // TODO
    handlerFactory: TichuWebSocketHandlerFactory
  ) {
    this.handler = handlerFactory();

    // default event handlers
    const defaultOnOpen = (event: OnOpenParams) => {
      this.debug("ws onopen", event);
      this.handler.onConnect();
    };
    this.onOpens = [defaultOnOpen];

    const defaultOnClose = (event: OnCloseParams) => {
      this.debug("ws onclose:", event);
      this.handler.onConnectionClose(event.code, event.reason, event.wasClean);
    };
    this.onCloses = [defaultOnClose];

    const defaultOnError = (event: OnErrorParams) => {
      this.debug("ws onerror:", event);
      this.handler.onWebsocketError(event.message, event.error);
    };
    this.onErrors = [defaultOnError];

    const defaultOnReceive = (event: OnReceiveParams) => {
      this.receive(event.data);
    };
    this.onReceives = [defaultOnReceive];

    this.onSends = [];

    // message tracker
    this.waitingForResponse = new Map<string, OnResponse>();
  }

  registerOnOpen(onOpen: OnOpen) {
    this.onOpens = [...this.onOpens, onOpen];
  }

  registerOnClose(onClose: OnClose) {
    this.onCloses = [...this.onCloses, onClose];
  }

  registerOnError(onError: OnError) {
    this.onErrors = [...this.onErrors, onError];
  }

  registerOnSend(onSend: OnSend) {
    this.onSends = [...this.onSends, onSend];
  }

  registerOnReceive(onReceive: OnReceive) {
    this.onReceives = [...this.onReceives, onReceive];
  }

  // TODO move url (or room id...) to constructor
  connect(url: string): WSTichuClient {
    this.webSocket = this.webSocketSetup(url);
    return this;
  }

  isConnected(): boolean {
    return this.ws().readyState === WebSocket.OPEN;
  }

  // After connection, only actions should be play or pass
  // and its maybe just a single question that lists cards and a special key/option to pass
  // .. or just, if no card selected > pass
  // and a confirmation
  //
  // https://www.npmjs.com/package/read has a timeout function which could also be interesting
  // https://www.npmjs.com/package/https-proxy-agent could be needed as well

  send = (msg: OutgoingMessage, onResponse: OnResponse = NOOP) => {
    this.waitingForResponse.set(msg.txId, onResponse);
    const msgJson = JSON.stringify(msg);
    this.debug("Sending", msg);
    this.ws().send(msgJson);
    this.onSends.forEach((f) => f(msg));
  };

  close = () => {
    this.ws().close();
  };

  private receive = (data: WebSocketData) => {
    this.handler.beforeMessageProcessing();

    // there's gotta be a better way than just casting to string
    const msg = JSON.parse(data as string) as IncomingMessage;
    // this.debug("Received", msg);

    const isResponse = this.removeFromResponseQueue(msg);

    // Game messages are handled more specifically in handleGameMessage before being
    // delegated to handler - other simpler messages are delegated straightaway.
    visitEnumValue(msg.messageType).with({
      game: () =>
        this.handleGameMessage(msg as IncomingGameMessage, isResponse),
      hand: () => this.handler.handleHandMessage(msg as IncomingHandMessage),
      chat: () => this.handler.handleChatMessage(msg as IncomingChatMessage),
      activity: () =>
        this.handler.handleActivityMessage(msg as ActivityMessage),
      error: () => this.handler.handleErrorMessage(msg as ErrorMessage),
    });

    this.handler.afterMessageProcessing(this.send);
  };

  private handleGameMessage(msg: IncomingGameMessage, isResponse: boolean) {
    visitEnumValue(msg.forAction).with({
      init: () => {},
      join: () => {
        visitEnumValue(msg.result as JoinResult).with(
          this.handler.handleJoin(isResponse)
        );
      },
      ready: () => {
        visitEnumValue(msg.result as PlayerIsReadyResult).with(
          this.handler.handleReady(isResponse)
        );
      },
      "new-trick": () => {
        this.debug("What do we do here?"); // TODO
      },
      play: () => {
        const msg1 = msg as IncomingPlayerPlaysResponse;
        visitEnumValue(msg1.result as PlayResult).with(
          this.handler.handlePlayResult(isResponse, msg1)
        );
      },
    });
  }

  // TODO add tests -- likely needs to be moved out of this class
  private removeFromResponseQueue(msg: IncomingMessage) {
    if (!msg.txId) {
      return false;
    }
    const isResponse = this.waitingForResponse.has(msg.txId);
    if (isResponse) {
      this.debug(
        `Removing ${msg.txId} from message queue - remaining:`,
        this.waitingForResponse.keys()
      );
      // Invoke callback
      this.waitingForResponse.get(msg.txId)!();
      // Remove entry from queue
      this.waitingForResponse.delete(msg.txId);
      this.debug(
        `Removed ${msg.txId} from message queue - remaining:`,
        this.waitingForResponse.keys()
      );
    }
    return isResponse;
  }

  private ws(): WebSocketTypes {
    if (!this.webSocket) {
      throw new Error("Websocket is not setup!");
    }
    return this.webSocket;
  }

  private webSocketSetup(url: string): WebSocketTypes {
    const ws = this.newWebsocket(url);

    ws.onopen = (event: OnOpenParams) => {
      this.onOpens.forEach((f) => f(event));
    };

    ws.onclose = (e: OnCloseParams) => {
      this.onCloses.forEach((f) => f(e));
    };

    ws.onerror = (e: OnErrorParams) => {
      this.onErrors.forEach((f) => f(e));
    };

    ws.onmessage = (e: OnReceiveParams) => {
      console.log("onmessage:", e);
      console.log("this.onReceives:", this.onReceives);
      this.onReceives.forEach((f) => f(e));
    };

    // browser websocket API doesn't support handling ping/pong
    // ws.on("ping", this.handler.onPing);
    // ws.on("pong", this.handler.onPong);

    return ws;
  }

  protected newWebsocket(url: string) {
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
    return ws;
  }

  private debug = (...msg: any) => {
    this.handler.debug(msg);
  };
}
