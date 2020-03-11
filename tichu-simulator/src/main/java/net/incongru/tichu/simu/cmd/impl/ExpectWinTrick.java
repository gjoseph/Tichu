package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult.AbstractPlayResult;
import net.incongru.tichu.model.Player;
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
    protected void doExec(SimulatedGameContext ctx, AbstractPlayResult result) {
        super.doExec(ctx, result);

        final Trick trick = ctx.game().currentRound().currentTrick();

        final Player lastPlayer = trick.previousNonPass().player();
        final boolean match = trick.isDone() && lastPlayer.name().equals(expectedPlayerName);
        if (match) {
            ctx.log("Trick is done and won by %s, as expected.", expectedPlayerName);
        } else {
            ctx.log("Trick is %, last player is %s", trick.isDone() ? "done" : "not done", lastPlayer);
            throw new Simulation.PostActionCommandException("Trick was expected to be 'done' at this point, with %s as the winner", expectedPlayerName);
        }

    }
}
