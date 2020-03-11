package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult.AbstractPlayResult;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectPlayResult extends AbstractExpectResult<AbstractPlayResult> {

    private final PostActionCommandFactory.ExpectablePlayResult expectedPlayResult;

    ExpectPlayResult(PostActionCommandFactory.ExpectablePlayResult expectedPlayResult) {
        this.expectedPlayResult = expectedPlayResult;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, AbstractPlayResult result) {
        final Play.PlayResult.Result actualResult = result.playResult().result();
        boolean match = expectedPlayResult.test(actualResult);
        if (match) {
            ctx.log("Play was %s, as expected", expectedPlayResult);
        } else {
            throw new Simulation.PostActionCommandException("Play was expected to result in %s, but resulted in %s instead", expectedPlayResult, actualResult);
        }
    }

    @Override
    protected String expectedVerb() {
        return "result";
    }

    @Override
    protected Class<AbstractPlayResult> expectedResult() {
        return AbstractPlayResult.class;
    }
}