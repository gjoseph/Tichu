import { UserId } from "tichu-client-ts-lib";

export enum RoomStatus {
  OPEN,
  CONNECTED,
  NEED_PLAYERS,
  WAIT_FOR_READY,
  PLAYING,
  FINISHED,
}

enum RoomUserStatus {
  WATCHING,
  PLAYING,
}

class RoomUser {
  constructor(
    readonly status: RoomUserStatus,
    readonly user: UserId,
    readonly team: undefined | 0 | 1
  ) {}
}

export class RoomState {
  readonly users: RoomUser[] = [];
  constructor(readonly status: RoomStatus) {}

  // Do we setup teams before a GameState?
}
