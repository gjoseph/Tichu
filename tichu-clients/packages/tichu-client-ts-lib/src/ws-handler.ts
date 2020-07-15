import {
  ActivityMessage,
  ErrorMessage,
  IncomingChatMessage,
  IncomingHandMessage,
  IncomingPlayerPlaysResponse,
  JoinResult,
  PlayerIsReadyResult,
  PlayResult,
} from "./messages";
import { EnumValueVisitorCore } from "ts-enum-util";
import { SendFunction } from "./ws-client";

/**
 * WSTichuClient calls this to get an implementation of TichuWebSocketHandler.
 */
export type TichuWebSocketHandlerFactory = () => TichuWebSocketHandler;

export interface TichuWebSocketHandler {
  // ==== Websocket callbacks
  // Callbacks are exposed as properties type as arrow functions,
  // not methods, such that they can be referenced as "listeners"
  // in ws callbacks and still have the current this bindings without
  // too much hassle
  onConnect: () => void;
  onConnectionClose: (code: number, reason: string, wasClean: boolean) => void;
  /**
   * Browser websocket error events don't have a message or error object
   */
  onWebsocketError: (message?: string, error?: any) => void;
  onPing: () => void;
  onPong: () => void;

  // ==== Message handling
  beforeMessageProcessing(): void;

  /**
   * The Handler can optionally elect to send a message, potentially as a reaction/response to the processed message.
   */
  afterMessageProcessing(send: SendFunction): void;

  handleChatMessage(msg: IncomingChatMessage): void;
  handleActivityMessage(msg: ActivityMessage): void;
  handleErrorMessage(msg: ErrorMessage): void;
  handleHandMessage(msg: IncomingHandMessage): void;

  // ==== Game message visitors
  // isResponse is really "isResponseFromCurrentPlayer" but yikes that's long
  handleJoin: (isResponse: boolean) => EnumValueVisitorCore<JoinResult, void>;
  handleReady: (
    isResponse: boolean
  ) => EnumValueVisitorCore<PlayerIsReadyResult, void>;
  handlePlayResult: (
    isResponse: boolean,
    msg: IncomingPlayerPlaysResponse
  ) => EnumValueVisitorCore<PlayResult, void>;

  // ==== Varia
  debug: (...msg: any) => void;
}
