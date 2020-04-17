package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectEndOfRound implements Simulation.PostActionCommand {
    ExpectEndOfRound() {
    }

    @Override
    public void exec(SimulatedGameContext ctx, ActionResponse response) {
        final boolean roundIsDone = ctx.game().currentRound().isDone();
        if (roundIsDone) {
            ctx.log("Round is done, as expected.");
        } else {
            ctx.log("Round is not done.");
            throw new Simulation.PostActionCommandException("Round is not done");
        }
    }
}
