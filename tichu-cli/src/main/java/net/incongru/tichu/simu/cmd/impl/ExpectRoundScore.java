package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;

class ExpectRoundScore implements Simulation.PostActionCommand {
    private final String teamName;
    private final int expectedScore;

    ExpectRoundScore(String teamName, int expectedScore) {
        this.teamName = teamName;
        this.expectedScore = expectedScore;
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        throw new IllegalStateException("Not implemented yet");
    }
}
