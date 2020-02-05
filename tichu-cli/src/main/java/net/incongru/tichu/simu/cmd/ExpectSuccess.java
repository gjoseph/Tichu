package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.Simulation;

public class ExpectSuccess implements Simulation.PostActionCommand {
    @Override
    public void exec(Action.Result result) {
        if (!(result instanceof Action.Success)) {
            throw new Simulation.PostActionCommandException("Action expected to succeed but failed: " + result);
        }
    }
}
