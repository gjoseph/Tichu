package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectPlayResult extends AbstractExpectResult<Action.PlayResult> {

    private final PostActionCommandFactory.ExpectablePlayResult expectedPlayResult;

    ExpectPlayResult(PostActionCommandFactory.ExpectablePlayResult expectedPlayResult) {
        this.expectedPlayResult = expectedPlayResult;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, Action.PlayResult result) {
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
        return "play";
    }

    @Override
    protected Class<Action.PlayResult> expectedResult() {
        return Action.PlayResult.class;
    }
}
