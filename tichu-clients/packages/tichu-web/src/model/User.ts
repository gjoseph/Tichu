import { UserId } from "tichu-client-ts-lib";

export class User {
  constructor(readonly id: UserId, readonly name: string) {}
}
