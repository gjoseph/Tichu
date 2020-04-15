package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.impl.PlayerPlaysActionResponse;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectWinTrick extends ExpectPlayResult {
    private final UserId expectedPlayer;

    ExpectWinTrick(UserId expectedPlayer) {
        super(PostActionCommandFactory.ExpectablePlayResult.TrickEnd);
        this.expectedPlayer = expectedPlayer;
    }

    @Override
    protected void doExec(SimulatedGameContext ctx, PlayerPlaysActionResponse response) {
        super.doExec(ctx, response);

        final Trick trick = ctx.game().currentRound().currentTrick();

        final Player lastPlayer = trick.previousNonPass().player();
        final boolean match = trick.isDone() && lastPlayer.id().equals(expectedPlayer);
        if (match) {
            ctx.log("Trick is done and won by %s, as expected.", expectedPlayer);
        } else {
            ctx.log("Trick is %, last player is %s", trick.isDone() ? "done" : "not done", lastPlayer);
            throw new Simulation.PostActionCommandException("Trick was expected to be 'done' at this point, with %s as the winner", expectedPlayer);
        }

    }
}
