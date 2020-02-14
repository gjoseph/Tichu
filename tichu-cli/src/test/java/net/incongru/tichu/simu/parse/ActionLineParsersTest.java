package net.incongru.tichu.simu.parse;

import com.google.common.collect.Sets;
import net.incongru.tichu.action.ActionFactory;
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

import java.util.stream.Stream;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ActionLineParsersTest {

    // We return mocks for everything, all we want to check is that the correct factory method is called
    // so we just call verify() which is slightly less verbose
    @Mock(answer = Answers.RETURNS_MOCKS)
    private ActionFactory actionFactory;

    private ActionLineParsers parsers;

    @BeforeEach
    void setUp() {
        parsers = new ActionLineParsers(actionFactory);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(actionFactory);
    }

    @Test
    public void throwsOnUnknownAction() {
        Exception exception = assertThrows(LineParserException.class, () -> parsers.parse(t("ice cream")));
        assertThat(exception.getMessage()).contains("[ice cream]");
        assertThat(exception.getMessage()).contains("unrecognised action");
    }

    @Test
    public void recognisesInitAction() {
        assertThat(parsers.parse(t("init"))).isNotNull();
        verify(actionFactory).init();
    }

    @ParameterizedTest
    @MethodSource
    public void joinTeamActionIs0Indexed(String txt, String expectedPlayerName, int expectedTeamIndex) {
        assertThat(parsers.parse(t(txt))).isNotNull();
        verify(actionFactory).joinTeam(expectedPlayerName, expectedTeamIndex);
    }

    static Stream<Arguments> joinTeamActionIs0Indexed() {
        return Stream.of(
                arguments("alex joins team 1", "alex", 0),
                arguments("charlie joins team 2", "charlie", 1)
        );
    }

    @Test
    public void recognisesPlayerIsReady() {
        assertThat(parsers.parse(t("jules is ready"))).isNotNull();
        verify(actionFactory).isReady("jules");
    }

    @Test
    public void recognisesCheatDeal() {
        assertThat(parsers.parse(t("cheat-deal quinn _1, r2, b3,k4, g5,       r6,b7,k8,g9,g10 ,bk,kk,gk,ra"))).isNotNull();
        verify(actionFactory).cheatDeal("quinn", Sets.newHashSet(MahJong, R2, B3, K4, G5, R6, B7, K8, G9, G10, BK, KK, GK, RA));
    }

    @Test
    public void recognisesPlayerPlays() {
        assertThat(parsers.parse(t("alex plays _1,r2,b3,k4,g5,r6,b7,k8"))).isNotNull();
        verify(actionFactory).plays("alex", Sets.newHashSet(MahJong, R2, B3, K4, G5, R6, B7, K8));
    }

    private TokenisedLine t(String line) {
        return new TokenisedLine(line);
    }

}
