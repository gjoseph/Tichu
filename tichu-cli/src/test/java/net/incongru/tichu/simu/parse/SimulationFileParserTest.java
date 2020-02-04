package net.incongru.tichu.simu.parse;

import net.incongru.tichu.simu.PathUtil;
import net.incongru.tichu.simu.Simulation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationFileParserTest {
    @Test
    @Disabled
    public void sampleIsLoadable() {
        final Simulation simu = assertDoesNotThrow(
                () -> new SimulationFileParser().parse(PathUtil.resource("/SampleScriptedGame.tichu"))
        );
        final List<Simulation.ActionAndCommands> actionAndExpectations = simu.actionAndCommands();
        assertThat(actionAndExpectations).hasSize(28);
        assertThat(actionAndExpectations.get(0).action().getClass().getSimpleName()).isEqualTo("InitialiseGame");
        assertThat(actionAndExpectations.get(27).action().getClass().getSimpleName()).isEqualTo("PlayerPlays");
        assertThat(actionAndExpectations.get(27).commands()).hasSize(4);
    }
}