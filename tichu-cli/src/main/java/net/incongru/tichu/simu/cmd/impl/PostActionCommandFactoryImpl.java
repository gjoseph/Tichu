package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

public class PostActionCommandFactoryImpl implements PostActionCommandFactory {
    @Override
    public Simulation.PostActionCommand expectSuccess() {
        return new ExpectSuccess();
    }

    @Override
    public Simulation.PostActionCommand expectInvalidPlay(String expectedError) {
        return new ExpectInvalidPlay(expectedError);
    }

    @Override
    public Simulation.PostActionCommand expectError(String expectedError) {
        return new ExpectError(expectedError);
    }
}
