package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.simu.Simulation;

public interface PostActionCommandFactory {
    Simulation.PostActionCommand expectSuccess();

    Simulation.PostActionCommand expectInvalidPlay(String expectedError); // TODO is there an enum of these?

    Simulation.PostActionCommand expectError(String expectedError);
}
