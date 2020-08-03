import { Card, CardShortName } from "./cards";
import { UserId } from "./model";
import { nanoid } from "nanoid";

interface Message {
  readonly messageType: "activity" | "game" | "hand" | "chat" | "error";
}

// Some IncomingMessage have a txId (see impls), all OutgoingMessage have a txId
export type IncomingMessage = Message & { txId?: string };
export type OutgoingMessage = Message & { txId: string };

export class ActivityMessage implements IncomingMessage {
  readonly messageType = "activity";

  constructor(readonly actor: UserId, readonly activity: ActivityType) {}
}

export class IncomingGameMessage implements IncomingMessage {
  readonly messageType = "game";

  constructor(
    readonly txId: string,
    readonly forAction: ActionType,
    readonly actor: UserId,
    readonly result: IncomingResult, // depends on action, server impls are net.incongru.tichu.action.ActionResponse.Result
    readonly message: ActionResultMessage
  ) {}
}

export class IncomingPlayerPlaysResponse extends IncomingGameMessage {
  constructor(
    readonly txId: string,
    readonly forAction: ActionType,
    readonly actor: UserId,
    readonly result: IncomingResult,
    readonly message: ActionResultMessage,
    // readonly  play: Play,
    readonly nextPlayer: UserId
  ) {
    super(txId, forAction, actor, result, message);
  }
}

export class IncomingHandMessage implements IncomingMessage {
  readonly messageType = "hand";

  constructor(
    readonly txId: string,
    readonly hand: { cards: CardShortName[] }
  ) {}
}

export class IncomingChatMessage implements IncomingMessage {
  readonly messageType = "chat";

  constructor(readonly from: UserId, readonly content: string) {}
}

export class ErrorMessage implements IncomingMessage {
  readonly messageType = "error";

  constructor(
    readonly actor: UserId,
    readonly txId: string,
    readonly traceId: string
  ) {}
}

export class OutgoingGameMessage implements OutgoingMessage {
  readonly messageType = "game";

  constructor(readonly action: GameParam, readonly txId: string = nanoid()) {}
}

export class OutgoingChatMessage implements OutgoingMessage {
  readonly messageType = "chat";

  constructor(readonly content: string, readonly txId: string = nanoid()) {}
}

type ActivityType = "coco" | "disconnected";
type ActionType = "init" | "join" | "new-trick" | "ready" | "play";
type IncomingResult = JoinResult | PlayerIsReadyResult | PlayResult;
export type JoinResult =
  | "can-not-join-full-table"
  | "ok"
  | "ok-table-is-now-full";
export type PlayerIsReadyResult = "ok" | "ok-started";
export type PlayResult =
  | "next-player-goes"
  | "trick-end"
  | "too-weak"
  | "not-in-hand"
  | "invalid-play"
  | "invalid-state";

type ActionResultMessage = string;

interface GameParam {
  readonly type: ActionType;
}

export class InitParam implements GameParam {
  readonly type = "init";

  constructor() {}
}

// join
export class JoinParam implements GameParam {
  readonly type = "join";

  constructor(readonly team: number) {}
}

// newTrick
export class NewTrickParam implements GameParam {
  readonly type = "new-trick";

  constructor() {}
}

// isReady
export class PlayerIsReadyParam implements GameParam {
  readonly type = "ready";

  constructor() {}
}

export class PlayerPlaysParam implements GameParam {
  readonly type = "play";

  static fromShortNames(shortNames: CardShortName[]) {
    return new PlayerPlaysParam(shortNames);
  }

  static fromCards(fullObjects: Card[]) {
    return new PlayerPlaysParam(fullObjects.map((c) => c.shortName));
  }

  private constructor(readonly cards: CardShortName[]) {}
}
