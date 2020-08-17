import { IncomingPlayerPlaysResponse } from "./messages";
import { TichuWebSocketHandler } from "./ws-handler";

const NOOP = () => {};

export class DummyTichuWebSocketHandler implements TichuWebSocketHandler {
  constructor() {}

  debug = NOOP;

  onConnect = NOOP;
  onConnectionClose = NOOP;
  onWebsocketError = NOOP;

  onPing = NOOP;
  onPong = NOOP;

  beforeMessageProcessing = NOOP;
  afterMessageProcessing = NOOP;
  handleChatMessage = NOOP;
  handleActivityMessage = NOOP;
  handleErrorMessage = NOOP;
  handleStatusMessage = NOOP;
  handleHandMessage = NOOP;

  // ==== Game message visitors
  handleJoin = (isResponse: boolean) => {
    return {
      "can-not-join-full-table": NOOP,
      ok: NOOP,
      "ok-table-is-now-full": NOOP,
    };
  };

  handleReady = (isResponse: boolean) => {
    return {
      ok: NOOP,
      "ok-started": NOOP,
    };
  };

  handlePlayResult = (
    isResponse: boolean,
    msg: IncomingPlayerPlaysResponse
  ) => {
    return {
      "next-player-goes": NOOP,
      "trick-end": NOOP,
      "invalid-play": NOOP,
      "invalid-state": NOOP,
      "not-in-hand": NOOP,
      "too-weak": NOOP,
    };
  };
}
