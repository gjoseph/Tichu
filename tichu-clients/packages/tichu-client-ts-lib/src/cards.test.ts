import test from "ava";
import { cardFromName } from "./cards";

test("Cards have a type we can query", (t) => {
    t.is(cardFromName("*1").type, "special");
    t.is(cardFromName("R4").type, "normal");
});

test("cardFromName throws on unknown", (t) => {
    t.throws(() => cardFromName("DOES-NOT-EXIST"), {
        message: /.*unknown card.*DOES-NOT-EXIST/i,
    });
});
