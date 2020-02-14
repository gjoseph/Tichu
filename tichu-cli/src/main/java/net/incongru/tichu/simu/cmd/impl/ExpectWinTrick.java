package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectWinTrick implements Simulation.PostActionCommand {
    ExpectWinTrick() {
        super();
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }
}
