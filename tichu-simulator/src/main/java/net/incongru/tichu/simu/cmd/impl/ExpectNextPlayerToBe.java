package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectNextPlayerToBe implements Simulation.PostActionCommand {
    private final UserId expectedPlayer;

    ExpectNextPlayerToBe(UserId expectedPlayer) {
        this.expectedPlayer = expectedPlayer;
    }

    @Override
    public void exec(SimulatedGameContext ctx, ActionResponse response) {
        final Trick trick = ctx.game().currentRound().currentTrick();
        // "currentPlayer" should already have been set to next here
        final UserId nextPlayer = trick.currentPlayer().id();
        final boolean match = nextPlayer.equals(expectedPlayer);
        if (match) {
            ctx.log("Next player is %s, as expected.", expectedPlayer);
        } else {
            ctx.log("Next player is %s, we expected it to be %s", nextPlayer, expectedPlayer);
            throw new Simulation.PostActionCommandException("Next player is %s, we expected it to be %s", nextPlayer, expectedPlayer);
        }
    }
}
