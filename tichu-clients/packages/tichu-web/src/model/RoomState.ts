import { User } from "./User";

export enum RoomStatus {
  OPEN,
  CONNECTED,
  NEED_PLAYERS,
  WAIT_FOR_READY,
  PLAYING,
  FINISHED,
}

enum RoomUserStatus { // TODO align this and server GameStatusMessage.PlayerState
  WATCHING,
  PLAYING,
}

class RoomUser {
  constructor(
    readonly user: User,
    readonly status: RoomUserStatus,
    readonly team: undefined | 0 | 1,
  ) {}
}

export class RoomState {
  constructor(
    readonly status: RoomStatus,
    readonly users: RoomUser[] = [],
  ) {}

  // Do we setup teams before a GameState?
}
