package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.model.Score;
import net.incongru.tichu.simu.SimulatedGameContext;

class ExpectTotalScore extends AbstractExpectScore {

    ExpectTotalScore(Score expectedScore) {
        super(expectedScore);
    }

    @Override
    String scoreType() {
        return "Total";
    }

    @Override
    Score score(SimulatedGameContext ctx) {
        return ctx.game().globalScore();
    }
}
