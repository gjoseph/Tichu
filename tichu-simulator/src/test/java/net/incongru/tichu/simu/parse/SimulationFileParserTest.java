package net.incongru.tichu.simu.parse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.google.common.collect.Lists;
import java.util.List;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.simu.PathUtil;
import net.incongru.tichu.simu.Simulation;
import org.junit.jupiter.api.Test;

class SimulationFileParserTest {

    @Test
    void sampleIsLoadable() {
        final Simulation simu = assertDoesNotThrow(() ->
            new SimulationFileParser().parse(
                PathUtil.resource("/SampleScriptedGame.tichu")
            )
        );
        final List<Simulation.ActionAndCommands> actionAndExpectations =
            simu.actionAndCommands();
        assertThat(actionAndExpectations).hasSize(33);
        final ActionParam.WithActor actionParam = actionAndExpectations
            .getFirst()
            .actionParam();
        assertThat(actionParam).isInstanceOf(ActionParam.WithActor.class);
        assertThat(actionParam.actor()).isEqualTo(UserId.of("dummy")); // actor id for this action is currently hardcoded in ActionLineParsers
        assertThat(actionParam.param()).isInstanceOf(InitialiseGameParam.class);
        final Simulation.ActionAndCommands last = Lists.reverse(
            actionAndExpectations
        ).getFirst();
        assertThat(last.actionParam()).isInstanceOf(
            ActionParam.WithActor.class
        );
        assertThat(last.actionParam().actor()).isEqualTo(UserId.of("quinn"));
        assertThat(last.actionParam().param()).isInstanceOf(
            PlayerPlaysParam.class
        );
        assertThat(last.commands())
            .extracting(pac -> pac.getClass().getSimpleName())
            .containsExactly(
                "ExpectPlay",
                "DebugPlayerHand",
                "DebugPlayerHand",
                "DebugPlayerHand",
                "DebugPlayerHand"
                // "ExpectEndOfRound", "ExpectRoundScore", "ExpectTotalScore", "ExpectGameState"
            );
    }
}
