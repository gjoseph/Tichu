package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

public abstract class AbstractExpectResult<R extends Action.Result> implements Simulation.PostActionCommand {
    @Override
    public final void exec(SimulatedGameContext ctx, Action.Result result) {
        if (!(expectedResult().isInstance(result))) {
            throw new Simulation.PostActionCommandException("Action was expected to %s but: %s", expectedVerb(), result);
        }
        doExec(ctx, (R) result);
    }

    protected abstract void doExec(SimulatedGameContext ctx, R result);

    protected abstract String expectedVerb();

    protected abstract Class<R> expectedResult();
}
