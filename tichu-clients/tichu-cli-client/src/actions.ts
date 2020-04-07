// init
import { Card } from "./cards";

export interface Message {
    readonly type: "chat" | "game";
}

export class ChatMessage {
    readonly type = "chat";

    constructor(readonly content: string) {}
}

export class GameMessage {
    readonly type = "game";

    constructor(readonly action: GameParam) {}
}

interface GameParam {}

export class InitParam implements GameParam {
    constructor() {}
}

// join
export class JoinParam {
    type: string = "join";

    constructor(readonly playerName: string, readonly team: number) {}
}

// newTrick?

// isReady
export class PlayerIsReadyParam {
    constructor(readonly playerName: string) {}
}

// play
export class PlayerPlaysParam {
    constructor(readonly playerName: string, readonly cards: Card[]) {}
}

// pass = play[]

export enum Actions {
    init = "init",
    join = "join",
    ready = "ready",
    play = "play",
    pass = "pass"
}
