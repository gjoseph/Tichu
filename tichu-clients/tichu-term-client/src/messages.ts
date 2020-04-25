// init
import { Card } from "./cards";
import { nanoid } from "nanoid";

interface Message {
    readonly messageType: "chat" | "game";
}

export type IncomingMessage = Message;
export type OutgoingMessage = Message & { txId: string };

export class IncomingChatMessage implements IncomingMessage {
    readonly messageType = "chat";

    constructor(readonly content: string) {}
}

export class OutgoingChatMessage implements OutgoingMessage {
    readonly messageType = "chat";

    constructor(readonly content: string, readonly txId: string = nanoid()) {}
}

export class IncomingGameMessage implements IncomingMessage {
    readonly messageType = "game";

    constructor(
        readonly txId: string,
        readonly forAction: ActionType,
        readonly actor: Actor,
        readonly result: IncomingResult, // depends on action, server impls are net.incongru.tichu.action.ActionResponse.Result
        readonly message: ActionResultMessage
    ) {}
}

export class OutgoingGameMessage implements OutgoingMessage {
    readonly messageType = "game";

    constructor(readonly action: GameParam, readonly txId: string = nanoid()) {}
}

type ActionType = "init" | "join" | "newTrick" | "ready" | "play";
type IncomingResult =
    // join:
    | "can-not-join-full-table"
    | "ok"
    | "ok-table-is-now-full"
    // playerIsReady
    | "ok"
    | "ok-started";
type Actor = string;
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
