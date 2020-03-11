package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult.Error;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectError extends AbstractExpectResult<Error> {
    private final String expectedError;

    ExpectError(String expectedError) {
        this.expectedError = expectedError;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, Error result) {
        if (expectedError.equals(result.error())) {
            ctx.log("Action indeed failed with '%s', as expected", expectedError);
        } else {
            throw new Simulation.PostActionCommandException("Expected '%s' error, but got unexpected error: %s", expectedError, result.error());
        }
    }

    @Override
    protected String expectedVerb() {
        return "fail";
    }

    @Override
    protected Class<Error> expectedResult() {
        return Error.class;
    }
}
