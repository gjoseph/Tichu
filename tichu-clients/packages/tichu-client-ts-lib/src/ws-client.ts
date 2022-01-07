import {
  ActivityMessage,
  ErrorMessage,
  IncomingChatMessage,
  IncomingGameMessage,
  IncomingGameStatusMessage,
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
import {
  Callback,
  EventDispatcher,
  EventParam,
  EventType,
  OnCloseParams,
  OnErrorParams,
  OnOpenParams,
  OnReceiveParams,
  OnSendParams,
} from "./events";
import { newWebsocket, WebSocketTypes } from "./ws-impl-wrapper";

export type SendFunction = (msg: OutgoingMessage) => void;

// ws supports string | Buffer | ArrayBuffer | Buffer[] -- not sure if/how to deal with binary messages, and if that's useful
export type WebSocketData = string;

/**
 * A function to callback when receiving a response to a particular message.
 */
export type OnResponse = () => void;

const NOOP = () => {};

export class WSTichuClient {
  private readonly handler: TichuWebSocketHandler;
  private readonly eventDispatcher = new EventDispatcher();
  private webSocket: WebSocketTypes | undefined;

  /**
   * Message IDs we sent and expect a response about.
   */
  private readonly waitingForResponse: Map<string, OnResponse>;

  constructor(
    readonly bogusCredentials: string, // TODO
    handlerFactory: TichuWebSocketHandlerFactory
  ) {
    this.handler = handlerFactory();

    // default event handlers TODO i think we can now replace a large portion of ws-handler with this event dispatcher
    this.eventDispatcher.on("connect", (event: OnOpenParams) => {
      this.debug("ws onopen", event);
      this.handler.onConnect();
    });
    this.eventDispatcher.on("disconnect", (event: OnCloseParams) => {
      this.debug("ws onclose:", event);
      this.handler.onConnectionClose(event.code, event.reason, event.wasClean);
    });
    this.eventDispatcher.on("error", (event: OnErrorParams) => {
      this.debug("ws onerror:", event);
      this.handler.onWebsocketError(event.message, event.error);
    });
    this.eventDispatcher.on("receive", (event: OnReceiveParams) => {
      this.receive(event.data);
    });

    // message tracker
    this.waitingForResponse = new Map<string, OnResponse>();
  }

  on<E extends EventType, P extends EventParam<E>>(
    eventType: E,
    callback: Callback<E, P>
  ) {
    this.eventDispatcher.on(eventType, callback);
  }

  // TODO move url (or room id...) to constructor
  connect(url: string): WSTichuClient {
    this.webSocket = this.webSocketSetup(url);
    return this;
  }

  isConnected(): boolean {
    return this.ws().isConnected();
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
    this.eventDispatcher.dispatch("send", { message: msg } as OnSendParams);
  };

  close = () => {
    this.ws().close();
  };

  private receive = (data: WebSocketData) => {
    this.handler.beforeMessageProcessing();

    // there's gotta be a better way than just casting to string
    const msg = JSON.parse(data as string) as IncomingMessage;
    this.debug("Received", msg);

    // TODO this will invoke the callback registered on send - should that move to after the message is actually processed?
    const isResponse = this.removeFromResponseQueue(msg);

    // Game messages are handled more specifically in handleGameMessage before being
    // delegated to handler - other simpler messages are delegated straightaway.
    visitEnumValue(msg.messageType).with({
      // TODO instead of delegating to handler, fire events?
      game: () =>
        this.handleGameMessage(msg as IncomingGameMessage, isResponse),
      status: () =>
        this.handler.handleStatusMessage(msg as IncomingGameStatusMessage),
      hand: () => this.handler.handleHandMessage(msg as IncomingHandMessage),
      chat: () => this.handler.handleChatMessage(msg as IncomingChatMessage),
      activity: () =>
        this.handler.handleActivityMessage(msg as ActivityMessage),
      error: () => this.handler.handleErrorMessage(msg as ErrorMessage),
      // unknown messageType yield EnumValueVisitee.js:50 Uncaught Error: Unexpected value: undefined
    });

    this.handler.afterMessageProcessing(this.send);
  };

  private handleGameMessage(msg: IncomingGameMessage, isResponse: boolean) {
    // TODO here could e.g fire a generic game event, visit the enum and fire more specific events

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
    const ws = newWebsocket(url, this.bogusCredentials);

    ws.onopen = (e: OnOpenParams) =>
      this.eventDispatcher.dispatch("connect", e);

    ws.onclose = (e: OnCloseParams) => {
      this.eventDispatcher.dispatch("disconnect", e);
    };

    ws.onerror = (e: OnErrorParams) => {
      this.eventDispatcher.dispatch("error", e);
    };

    ws.onmessage = (e: OnReceiveParams) => {
      this.eventDispatcher.dispatch("receive", e);
    };

    // browser websocket API doesn't support handling ping/pong
    // ws.on("ping", this.handler.onPing);
    // ws.on("pong", this.handler.onPong);

    return ws;
  }

  private debug = (...msg: any) => {
    this.handler.debug(msg);
  };
}
