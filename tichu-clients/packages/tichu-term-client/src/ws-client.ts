import WebSocket from "isomorphic-ws";
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
} from "tichu-client-ts-lib";
import { GameOpts } from "./startup";
import { visitEnumValue } from "ts-enum-util";
import {
  TichuWebSocketHandlerFactory,
  TichuWebSocketHandler,
} from "./ws-handler";

export type SendFunction = (msg: OutgoingMessage) => void;

export class WSTichuClient {
  private readonly handler: TichuWebSocketHandler;
  private webSocket: WebSocket | undefined;

  /**
   * Message IDs we sent and expect a response about.
   * TODO: for some reason, we only track these for game messages - should we track
   * for all?
   */
  private waitingForAnswer: string[];

  constructor(
    readonly opts: GameOpts,
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

  waitUntilDone(): Status {
    return "Done";
  }

  send = (msg: OutgoingMessage) => {
    this.waitingForAnswer.push(msg.txId); // Do we care for chat messages?
    const msgJson = JSON.stringify(msg);
    this.debug(" Sending", msgJson);
    this.ws().send(msgJson);
  };

  receive = (data: WebSocket.Data) => {
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

  close = () => {
    this.ws().close();
  };

  private ws(): WebSocket {
    if (!this.webSocket) {
      throw new Error("Websocket is not setup!");
    }
    return this.webSocket;
  }

  private webSocketSetup(url: string): WebSocket {
    // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
    const userPass = `${this.opts.user}:${this.opts.user}`;
    const authHeaderValue = "Basic " + Buffer.from(userPass).toString("base64");
    const ws = new WebSocket(url, {
      headers: {
        Authorization: authHeaderValue,
      },
    }); // TODO wtf are subprotocols

    ws.on("open", this.handler.onConnect);
    ws.on("close", this.handler.onConnectionClose);
    ws.on("error", this.handler.onWebsocketError);

    // ws.on("message", (data: WebSocket.Data) => {
    //   this.receive(data);
    // });
    ws.on("message", this.receive);

    ws.on("ping", this.handler.onPing);
    ws.on("pong", this.handler.onPong);

    return ws;
  }

  private debug = (...msg: any) => {
    this.handler.debug(msg);
  };
}

export type Status = "Done" | "Not done";
