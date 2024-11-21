import { UserId } from "tichu-client-ts-lib";

// Assuming we'll be using something like Auth0, it'll provide an avatar url
export class User {
  constructor(
    readonly id: UserId,
    readonly displayName?: string,
    readonly avatarUrl?: string,
  ) {}

  initials(): string | null {
    const trimmed = this.displayName?.trim();
    if (!trimmed) {
      return null;
    }
    return trimmed
      .split(/\s+/)
      .slice(0, 2)
      .map((s) => s[0])
      .join("")
      .toUpperCase();
  }
}
