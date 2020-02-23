package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;

class ExpectSuccess extends AbstractExpectResult<Action.Success> {
    ExpectSuccess() {
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, Action.Success result) {
    }

    @Override
    protected String expectedVerb() {
        return "succeed";
    }

    @Override
    protected Class<Action.Success> expectedResult() {
        return Action.Success.class;
    }
}
