package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.Simulation;

class ExpectSuccess implements Simulation.PostActionCommand {
    ExpectSuccess() {
        super();
    }

    @Override
    public void exec(Action.Result result) {
        if (!(result instanceof Action.Success)) {
            throw new Simulation.PostActionCommandException("Action expected to succeed but failed: " + result);
        }
    }
}
