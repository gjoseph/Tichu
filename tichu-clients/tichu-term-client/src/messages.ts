// init
import { Card } from "./cards";
import { nanoid } from "nanoid";
import { UserId } from "./model";

interface Message {
    readonly messageType: "activity" | "game" | "chat" | "error";
}

// Some IncomingMessage have a txId (see impls), all OutgoingMessage have a txId
export type IncomingMessage = Message;
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
type ActionType = "init" | "join" | "newTrick" | "ready" | "play";
type IncomingResult =
    // join:
    | "can-not-join-full-table"
    | "ok"
    | "ok-table-is-now-full"
    // playerIsReady
    | "ok"
    | "ok-started";

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

// newTrick?

// isReady
export class PlayerIsReadyParam implements GameParam {
    readonly type = "ready";

    constructor() {}
}

export class PlayerPlaysParam implements GameParam {
    readonly type = "play";

    constructor(readonly cards: Card[]) {}
}

// pass = play[]

export enum Actions {
    init = "init",
    join = "join",
    ready = "ready",
    play = "play",
    pass = "pass",
}