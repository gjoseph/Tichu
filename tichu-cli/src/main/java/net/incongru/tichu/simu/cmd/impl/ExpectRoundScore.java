package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.model.Score;
import net.incongru.tichu.simu.SimulatedGameContext;

class ExpectRoundScore extends AbstractExpectScore {

    ExpectRoundScore(Score expectedScore) {
        super(expectedScore);
    }

    @Override
    String scoreType() {
        return "Round";
    }

    @Override
    Score score(SimulatedGameContext ctx) {
        return ctx.game().currentRound().score();
    }
}
