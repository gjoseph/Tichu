package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult.AbstractPlayResult;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectPlay extends AbstractExpectResult<AbstractPlayResult> {
    private final PostActionCommandFactory.ExpectablePlay expectedPlay;

    ExpectPlay(PostActionCommandFactory.ExpectablePlay expectedPlay) {
        this.expectedPlay = expectedPlay;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, AbstractPlayResult result) {
        final Play actualResult = result.playResult().play();
        boolean match = expectedPlay.test(actualResult);
        if (match) {
            ctx.log("Play was %s, as expected", expectedPlay);
        } else {
            throw new Simulation.PostActionCommandException("Play was expected to be a %s, but was a %s instead", expectedPlay, actualResult);
        }
    }

    @Override
    protected String expectedVerb() {
        return "play";
    }

    @Override
    protected Class<AbstractPlayResult> expectedResult() {
        return AbstractPlayResult.class;
    }
}
