package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectPlay implements Simulation.PostActionCommand {
    private final PostActionCommandFactory.TemporaryPlayNamesEnum play;

    ExpectPlay(PostActionCommandFactory.TemporaryPlayNamesEnum play) {
        this.play = play;
    }

    @Override
    public void exec(Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }
}
