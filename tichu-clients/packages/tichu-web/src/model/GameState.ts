import { Card, UserId } from "tichu-client-ts-lib";

enum GameStatus {
  WHAT,
}

export class GameState {
  readonly gameStatus: GameStatus = GameStatus.WHAT;
  constructor(
    // roundState? tableState?
    // currentPlayer only comes through net.incongru.tichu.action.impl.PlayerPlaysResponse#nextPlayer currently

    readonly currentPlayer: UserId | undefined,
    readonly hand: Array<Card>,
    readonly otherPlayers: {
      id: UserId;
      cardsInHand: number;
      cardsCollected: number;
    }[]
  ) {}

  // maybe the below is in RoomState
  // table: {
  //   id: "...",
  //   teams: [
  //     {
  //       name: "Team A",
  //       players: [
  //         { name: "Greg", id: "<some-uuid-probably" },
  //         { name: "Shane", id: "<>" },
  //       ],
  //     },
  //     {
  //       name: "Team B",
  //       players: [
  //         { name: "Isa", id: "" },
  //         { name: "Mikayla", id: "<>" },
  //       ],
  //     },
  //   ],
  // },
}
