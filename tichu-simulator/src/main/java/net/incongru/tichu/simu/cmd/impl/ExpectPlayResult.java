package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.impl.PlayerPlaysResponse;
import net.incongru.tichu.action.impl.PlayerPlaysResult;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectPlayResult extends AbstractExpectResult<PlayerPlaysResponse> {

    private final PostActionCommandFactory.ExpectablePlayResult expectedPlayResult;

    ExpectPlayResult(PostActionCommandFactory.ExpectablePlayResult expectedPlayResult) {
        this.expectedPlayResult = expectedPlayResult;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, PlayerPlaysResponse response) {
        final PlayerPlaysResult actualResult = response.result();
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
    protected Class<PlayerPlaysResponse> expectedResult() {
        return PlayerPlaysResponse.class;
    }
}
