package net.incongru.tichu.simu;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

class TichuSimulatorTest {

    @Test
    void sampleIsExecutable() throws IOException {
        try {
            new TichuSimulator().executeSimulation(PathUtil.resource("/SampleScriptedGame.tichu"));
        } catch (Simulation.PostActionCommandException e) {
            fail(e.getMessage());
        }
    }
}