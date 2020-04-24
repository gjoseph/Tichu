import test from "ava";
import { coerceTeamId } from "./util";

test("coerceTeamId() returns 0-indexed values", t => {
    t.is(coerceTeamId("1"), 0);
    t.is(coerceTeamId("2"), 1);
    t.is(coerceTeamId("3"), 0);
    t.is(coerceTeamId("0"), 1);
    t.is(coerceTeamId("-4"), 1);
});
