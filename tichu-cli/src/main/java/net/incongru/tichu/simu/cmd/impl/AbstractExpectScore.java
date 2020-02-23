package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Score;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

abstract class AbstractExpectScore implements Simulation.PostActionCommand {
    protected final Score expectedScore;

    public AbstractExpectScore(Score expectedScore) {
        this.expectedScore = expectedScore;
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        final Score score = score(ctx);
        final boolean match = score.equals(expectedScore);
        if (match) {
            ctx.log("%s score was %s, as expected", scoreType(), expectedScore);
        } else {
            ctx.log("%s score was %s, instead of the expected %s", scoreType(), score, expectedScore);
            throw new Simulation.PostActionCommandException("%s score was %s, instead of the expected %s", scoreType(), score, expectedScore);
        }
    }

    abstract String scoreType();

    abstract Score score(SimulatedGameContext ctx);
}
