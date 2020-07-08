import { Card } from "tichu-client-ts-lib";
import { User } from "./User";

enum GameStatus {
  WHAT,
}

export class GameState {
  readonly gameStatus: GameStatus = GameStatus.WHAT;
  readonly player: User = new User("x", "xyz"); // this is already in app's state, maybe not needed here?
  constructor(
    // roundState? tableState?
    readonly hand: Card[]
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
