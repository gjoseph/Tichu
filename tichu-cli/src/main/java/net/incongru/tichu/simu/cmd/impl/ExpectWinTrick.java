package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectWinTrick extends ExpectPlayResult {
    private final String expectedPlayerName;

    ExpectWinTrick(String expectedPlayerName) {
        super(PostActionCommandFactory.ExpectablePlayResult.TrickEnd);
        this.expectedPlayerName = expectedPlayerName;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, Action.AbstractPlayResult result) {
        super.doExec(ctx, result);

        final Trick trick = ctx.game().currentRound().currentTrick();
        final boolean match = trick.isDone() && trick.currentPlayer().name().equals(expectedPlayerName);
        if (match) {
            ctx.log("Trick is done, as expected. Current player is indeed %s.", expectedPlayerName);
        } else {
            ctx.log("Trick is %, current player is %s", trick.isDone()?"done":"not done", trick.currentPlayer());
            // TODO... oh no this isn't going to work. currentPlayer is not the winner, it's going to be the last pass-er
            //  (in some cases at least)
            throw new Simulation.PostActionCommandException("Trick was expected to be 'done' at this point, with %s as the winner");
        }

    }
}
