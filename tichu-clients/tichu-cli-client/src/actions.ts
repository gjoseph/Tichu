// init
import { Card } from "./cards";

export class InitParam {
    constructor() {}
}

// join
export class JoinParam {
    type: string = "join";
    constructor(readonly playerName: string, team: number) {}
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
