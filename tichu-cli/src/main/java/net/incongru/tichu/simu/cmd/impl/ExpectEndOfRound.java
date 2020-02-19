package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.Simulation;

class ExpectEndOfRound implements Simulation.PostActionCommand {
    ExpectEndOfRound() {
    }

    @Override
    public void exec(Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }
}
