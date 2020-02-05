package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.Simulation;

public class ExpectError implements Simulation.PostActionCommand {
    private final String expectedError;

    public ExpectError(String expectedError) {
        this.expectedError = expectedError;
    }

    @Override
    public void exec(Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String toString() {
        return "expect:error:" + expectedError;
    }
}
