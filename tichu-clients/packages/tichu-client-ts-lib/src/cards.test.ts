import { describe, it, expect } from "vitest";
import { cardFromName } from "./cards";

describe("Cards", () => {
  it("have a type we can query", () => {
    expect(cardFromName("*1").type).toBe("special");
    expect(cardFromName("R4").type).toBe("normal");
  });

  it("cardFromName throws on unknown", () => {
    expect(() => cardFromName("DOES-NOT-EXIST")).toThrow(
      /.*unknown card.*DOES-NOT-EXIST/i,
    );
  });
});
