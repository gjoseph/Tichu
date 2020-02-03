package net.incongru.tichu.simu;

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
        Exception exception = assertThrows(ActionLineParsers.LineParserException.class, () -> {
            parsers.parse("ice cream");
        });
        assertThat(exception).extracting(Throwable::getMessage).matches(s -> s.contains("[ice cream]"));
    }

    @Test
    public void recognisesInitAction() {
        assertThat(parsers.parse("init")).isNotNull();
        verify(actionFactory).init();
    }

    @ParameterizedTest
    @MethodSource
    public void joinTeamActionIs0Indexed(String txt, String expectedPlayerName, int expectedTeamIndex) {
        assertThat(parsers.parse(txt)).isNotNull();
        verify(actionFactory).joinTeam(expectedPlayerName, expectedTeamIndex);
    }

    static Stream<Arguments> joinTeamActionIs0Indexed() {
        return Stream.of(
                arguments("alex joins team 1", "alex", 0),
                arguments("charlie joins team 2", "charlie", 1)
        );
    }
}