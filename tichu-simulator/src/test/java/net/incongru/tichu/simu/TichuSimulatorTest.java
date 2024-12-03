package net.incongru.tichu.simu;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class TichuSimulatorTest {

    @Test
    void sampleIsExecutable() throws IOException {
        try {
            new TichuSimulator()
                .executeSimulation(
                    PathUtil.resource("/SampleScriptedGame.tichu")
                );
        } catch (Simulation.PostActionCommandException e) {
            fail(e.getMessage());
        }
    }
}
