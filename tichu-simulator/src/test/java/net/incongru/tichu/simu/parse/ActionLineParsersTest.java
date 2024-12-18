package net.incongru.tichu.simu.parse;

import static net.incongru.tichu.model.util.DeckConstants.B3;
import static net.incongru.tichu.model.util.DeckConstants.B7;
import static net.incongru.tichu.model.util.DeckConstants.BK;
import static net.incongru.tichu.model.util.DeckConstants.G10;
import static net.incongru.tichu.model.util.DeckConstants.G5;
import static net.incongru.tichu.model.util.DeckConstants.G9;
import static net.incongru.tichu.model.util.DeckConstants.GK;
import static net.incongru.tichu.model.util.DeckConstants.K4;
import static net.incongru.tichu.model.util.DeckConstants.K8;
import static net.incongru.tichu.model.util.DeckConstants.KK;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static net.incongru.tichu.model.util.DeckConstants.R2;
import static net.incongru.tichu.model.util.DeckConstants.R6;
import static net.incongru.tichu.model.util.DeckConstants.RA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.UserId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ActionLineParsersTest {

    // We return mocks for everything, all we want to check is that the correct factory method is called
    // so we just call verify() which is slightly less verbose
    @Mock(answer = Answers.RETURNS_MOCKS)
    private ActionFactory actionFactory;

    private ActionLineParsers parsers;

    @BeforeEach
    void setUp() {
        parsers = new ActionLineParsers();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(actionFactory);
    }

    @Test
    void throwsOnUnknownAction() {
        assertThatThrownBy(() -> parsers.parse(t("ice cream")))
            .isExactlyInstanceOf(LineParserException.class)
            .hasMessageContaining("[ice cream]")
            .hasMessageContaining("unrecognised action");
    }

    @Test
    void recognisesInitAction() {
        assertThat(parsers.parse(t("init")))
            .isInstanceOf(ActionParam.WithActor.class)
            .satisfies(withActor -> {
                assertThat(withActor.param()).isInstanceOf(
                    InitialiseGameParam.class
                );
            });
    }

    @ParameterizedTest
    @MethodSource
    void joinTeamActionIs0Indexed(
        String txt,
        String expectedPlayerName,
        int expectedTeamIndex
    ) {
        assertThat(parsers.parse(t(txt))).isEqualTo(
            JoinTableParam.withActor(
                UserId.of(expectedPlayerName),
                expectedTeamIndex
            )
        );
    }

    static Stream<Arguments> joinTeamActionIs0Indexed() {
        return Stream.of(
            arguments("alex joins team 1", "alex", 0),
            arguments("charlie joins team 2", "charlie", 1)
        );
    }

    @Test
    void recognisesPlayerIsReady() {
        assertThat(parsers.parse(t("jules is ready"))).isEqualTo(
            PlayerIsReadyParam.withActor(UserId.of("jules"))
        );
    }

    @Test
    void recognisesCheatDeal() {
        assertThat(
            parsers.parse(
                t(
                    "cheat-deal quinn _1, r2, b3,k4, g5,       r6,b7,k8,g9,g10 ,bk,kk,gk,ra"
                )
            )
        ).isEqualTo(
            CheatDealParam.withActor(
                UserId.of("quinn"),
                new HashSet<>(
                    Arrays.asList(
                        MahJong,
                        R2,
                        B3,
                        K4,
                        G5,
                        R6,
                        B7,
                        K8,
                        G9,
                        G10,
                        BK,
                        KK,
                        GK,
                        RA
                    )
                )
            )
        );
    }

    @Test
    void recognisesPlayerPlays() {
        assertThat(
            parsers.parse(t("alex plays _1,r2,b3,k4,g5,r6,b7,k8"))
        ).isEqualTo(
            PlayerPlaysParam.withActor(
                UserId.of("alex"),
                new HashSet<>(
                    Arrays.asList(MahJong, R2, B3, K4, G5, R6, B7, K8)
                )
            )
        );
    }

    @Test
    void recognisesPlayerPasses() {
        assertThat(parsers.parse(t("alex passes"))).isEqualTo(
            PlayerPlaysParam.withActor(
                UserId.of("alex"),
                Collections.emptySet()
            )
        );
    }

    @Test
    void shouldNotAcceptPlayerPlaysWithNoCard() {
        assertThatThrownBy(() -> parsers.parse(t("alex plays   ")))
            .isInstanceOf(LineParserException.class)
            .hasMessageContaining("use the '<player> passes' action instead");
    }

    private TokenisedLine t(String line) {
        return new TokenisedLine(line);
    }
}
