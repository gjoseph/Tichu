import { test, expect } from "vitest";
import { User } from "./User";

test("initials for simple 2 word name", () => {
  expect(new User("x", "Alex Charlie").initials()).toBe("AC");
});
test("initials for 1 word name should be the single letter", () => {
  expect(new User("x", "Charlie").initials()).toBe("C");
});
test("initials for longer name should be limited to 2", () => {
  expect(new User("x", "Alex Charlie Jules Quinn").initials()).toBe("AC");
});
test("initials for falsy name should be null", () => {
  expect(new User("x", "").initials()).toBeNull();
  expect(new User("x", "  ").initials()).toBeNull();
});
