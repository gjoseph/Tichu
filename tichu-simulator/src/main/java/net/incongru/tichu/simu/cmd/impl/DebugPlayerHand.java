package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class DebugPlayerHand implements Simulation.PostActionCommand {
    private final UserId player;

    DebugPlayerHand(UserId player) {
        this.player = player;
    }

    @Override
    public void exec(SimulatedGameContext ctx, ActionResponse response) {
        final String hand = ctx.player(player).hand().toDebugString();
        ctx.log("Debug: %s's cards: %s", player, hand);
    }
}
