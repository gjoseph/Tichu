package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectNextPlayerToBe implements Simulation.PostActionCommand {
    private final String expectedPlayerName;

    ExpectNextPlayerToBe(String expectedPlayerName) {
        this.expectedPlayerName = expectedPlayerName;
    }

    @Override
    public void exec(SimulatedGameContext ctx, ActionResult result) {
        final Trick trick = ctx.game().currentRound().currentTrick();
        // "currentPlayer" should already have been set to next here
        final String nextPlayer = trick.currentPlayer().name();
        final boolean match = nextPlayer.equals(expectedPlayerName);
        if (match) {
            ctx.log("Next player is %s, as expected.", expectedPlayerName);
        } else {
            ctx.log("Next player is %s, we expected it to be %s", nextPlayer, expectedPlayerName);
            throw new Simulation.PostActionCommandException("Next player is %s, we expected it to be %s", nextPlayer, expectedPlayerName);
        }
    }
}