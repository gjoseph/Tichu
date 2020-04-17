package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectError extends AbstractExpectResult<ActionResponse> {
    private final String expectedError;

    ExpectError(String expectedError) {
        this.expectedError = expectedError;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, ActionResponse response) {
        if (response.result().isSuccessful()) {
            throw new Simulation.PostActionCommandException("Expected '%s' to fail, but it succeeded with: %s", response.forAction(), response.result());
        }

        // TODO match message
        if (expectedError.equals(response.message().toString())) {
            ctx.log("Action indeed failed with '%s', as expected", expectedError);
        } else {
            throw new Simulation.PostActionCommandException("Expected '%s' error, but got unexpected error: %s", expectedError, response.message());
        }
    }

    @Override
    protected String expectedVerb() {
        return "fail";
    }

    @Override
    protected Class<ActionResponse> expectedResult() {
        return ActionResponse.class;
    }
}
