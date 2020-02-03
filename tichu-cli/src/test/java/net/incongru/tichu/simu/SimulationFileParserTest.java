package net.incongru.tichu.simu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationFileParserTest {
    @Test
    @Disabled
    public void sampleIsLoadable() {
        final Simulation simu = assertDoesNotThrow(
                () -> new SimulationFileParser().parse(PathUtil.resource("/SampleScriptedGame.tichu"))
        );
        System.out.println("simu = " + simu);
    }
}