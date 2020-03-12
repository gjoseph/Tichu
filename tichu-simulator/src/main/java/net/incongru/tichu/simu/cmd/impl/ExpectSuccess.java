package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult.Success;
import net.incongru.tichu.simu.SimulatedGameContext;

class ExpectSuccess extends AbstractExpectResult<Success> {
    ExpectSuccess() {
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, Success result) {
    }

    @Override
    protected String expectedVerb() {
        return "succeed";
    }

    @Override
    protected Class<Success> expectedResult() {
        return Success.class;
    }
}
