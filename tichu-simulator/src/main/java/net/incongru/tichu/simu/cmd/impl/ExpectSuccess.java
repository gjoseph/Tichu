package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectSuccess extends AbstractExpectResult<ActionResponse> {

    ExpectSuccess() {}

    @Override
    protected void doExec(SimulatedGameContext ctx, ActionResponse response) {
        if (!response.result().isSuccessful()) {
            throw new Simulation.PostActionCommandException(
                "Expected '%s' to succeed, but it failed with: %s",
                response.forAction(),
                response.result()
            );
        }
    }

    @Override
    protected String expectedVerb() {
        return "succeed";
    }

    @Override
    protected Class<ActionResponse> expectedResult() {
        return ActionResponse.class;
    }
}
