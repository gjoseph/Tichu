package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

abstract class AbstractExpectResult<R extends ActionResult> implements Simulation.PostActionCommand {
    @Override
    public final void exec(SimulatedGameContext ctx, ActionResult result) {
        if (!(expectedResult().isInstance(result))) {
            throw new Simulation.PostActionCommandException("Action was expected to %s but: %s", expectedVerb(), result);
        }
        doExec(ctx, (R) result);
    }

    protected abstract void doExec(SimulatedGameContext ctx, R result);

    protected abstract String expectedVerb();

    protected abstract Class<R> expectedResult();
}
