package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectInvalidPlay implements Simulation.PostActionCommand {
    private final String expectedError;

    ExpectInvalidPlay(String expectedError) {
        this.expectedError = expectedError;
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }

}
