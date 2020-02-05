package net.incongru.tichu.simu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TichuSimulatorTest {
    @Test
    @Disabled
    public void sampleIsExecutable() {
        assertDoesNotThrow(
                () -> new TichuSimulator().executeSimulation(PathUtil.resource("/SampleScriptedGame.tichu"))
        );
    }
}