// init
class InitParam {
    constructor() {}
}

// join
class JoinParam {
    constructor(readonly playerName: string, team: number) {}
}

// newTrick?

// isReady
class PlayerIsReadyParam {
    constructor(readonly playerName: string) {}
}

// play
class PlayerPlaysParam {
    constructor(readonly playerName: string, readonly cards: string[]) {}
}

// pass = play[]

export enum Actions {
    init = "init",
    join = "join",
    ready = "ready",
    play = "play",
    pass = "pass"
}
