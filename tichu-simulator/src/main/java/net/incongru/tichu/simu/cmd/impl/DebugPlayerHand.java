package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class DebugPlayerHand implements Simulation.PostActionCommand {
    private final String playerName;

    DebugPlayerHand(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void exec(SimulatedGameContext ctx, ActionResult result) {
        final String hand = ctx.player(playerName).hand().toDebugString();
        ctx.log("Debug: %s's cards: %s", playerName, hand);
    }
}
