package net.incongru.tichu.simu.parse;

import com.google.common.collect.Lists;
import net.incongru.tichu.simu.PathUtil;
import net.incongru.tichu.simu.Simulation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimulationFileParserTest {
    @Test
    public void sampleIsLoadable() {
        final Simulation simu = assertDoesNotThrow(
                () -> new SimulationFileParser().parse(PathUtil.resource("/SampleScriptedGame.tichu"))
        );
        final List<Simulation.ActionAndCommands> actionAndExpectations = simu.actionAndCommands();
        assertThat(actionAndExpectations).hasSize(33);
        assertThat(actionAndExpectations.get(0).action().getClass().getSimpleName()).isEqualTo("InitialiseGame");
        final Simulation.ActionAndCommands last = Lists.reverse(actionAndExpectations).get(0);
        assertThat(last.action().getClass().getSimpleName()).isEqualTo("PlayerPlays");
        assertThat(last.commands())
                .extracting(pac -> pac.getClass().getSimpleName())
                .containsExactly(
                        "ExpectPlay",
                        "DebugPlayerHand", "DebugPlayerHand", "DebugPlayerHand", "DebugPlayerHand"
                        // "ExpectEndOfRound", "ExpectRoundScore", "ExpectTotalScore"
                );
    }
}