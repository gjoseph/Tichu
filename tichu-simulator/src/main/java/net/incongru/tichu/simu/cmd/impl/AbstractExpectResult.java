package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

abstract class AbstractExpectResult<R extends ActionResponse> implements Simulation.PostActionCommand {
    @Override
    public final void exec(SimulatedGameContext ctx, ActionResponse response) {
        if (!(expectedResult().isInstance(response))) {
            throw new Simulation.PostActionCommandException("Action was expected to %s but: %s", expectedVerb(), response);
        }
        doExec(ctx, (R) response);
    }

    protected abstract void doExec(SimulatedGameContext ctx, R result);

    protected abstract String expectedVerb();

    protected abstract Class<R> expectedResult();
}
