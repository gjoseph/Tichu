package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectSuccess implements Simulation.PostActionCommand {
    ExpectSuccess() {
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        if (!(result instanceof Action.Success)) {
            throw new Simulation.PostActionCommandException("Action expected to succeed but failed: %s", result);
        }
    }
}
