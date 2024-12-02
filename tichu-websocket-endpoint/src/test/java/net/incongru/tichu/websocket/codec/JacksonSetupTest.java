package net.incongru.tichu.websocket.codec;

import static net.incongru.tichu.websocket.codec.JacksonSetup.kebab;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import java.util.Set;
import java.util.stream.Stream;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.util.DeckConstants;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JacksonSetupTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = JacksonSetup.setupJacksonMapper();
    }

    @Test
    void canSerCards() throws JsonProcessingException {
        final Card[] cards = { DeckConstants.Pagoda_4, DeckConstants.MahJong };

        assertThatJson(objectMapper.writeValueAsString(cards)).isEqualTo(
            "['B4', '*1']"
        );
    }

    @Test
    void canDeserCards() throws JsonProcessingException {
        final String json = "{\"cards\":[\"jade_ace\", \"*d\"]}";
        final CardArrayWrapper readCards = objectMapper.readValue(
            json,
            CardArrayWrapper.class
        );
        assertThat(readCards.cards).containsExactlyInAnyOrder(
            DeckConstants.Jade_Ace,
            DeckConstants.Dragon
        );
    }

    @Test
    void canSerUserId() throws JsonProcessingException {
        final UserId u = UserId.of("test-user");

        assertThatJson(objectMapper.writeValueAsString(u)).isEqualTo(
            "test-user"
        );
    }

    @Test
    void canDeserUserId() throws JsonProcessingException {
        final String json = "{\"user\": \"test-user\"}";
        final UserIdWrapper readUserId = objectMapper.readValue(
            json,
            UserIdWrapper.class
        );
        assertThat(readUserId.user).isEqualTo(UserId.of("test-user"));
    }

    @Test
    void deserUnknownCardResultsInJacksonException() {
        assertThatThrownBy(() -> objectMapper.readValue("\"foo\"", Card.class))
            .isInstanceOf(InvalidFormatException.class)
            .hasMessageContaining("foo is not a valid card name");
    }

    @Test
    void canSerValidActionParam() throws JsonProcessingException {
        final PlayerPlaysParam play = new PlayerPlaysParam(
            Set.of(DeckConstants.Star_Ace, DeckConstants.B2)
        );
        final ActionParamWrapper actionParamWrapper = new ActionParamWrapper(
            play
        );
        assertThatJson(objectMapper.writeValueAsString(actionParamWrapper))
            .when(Option.IGNORING_ARRAY_ORDER)
            .isEqualTo("{action: {type: 'play', cards: ['RA', 'B2']}}");
    }

    @Test
    @Disabled("https://github.com/FasterXML/jackson-databind/issues/436")
    void rejectSerOfUnregisteredActionParamType() {
        final ActionParam cheat = new CheatDealParam(
            Set.of(DeckConstants.Star_Ace, DeckConstants.B2)
        );
        final ActionParamWrapper actionParamWrapper = new ActionParamWrapper(
            cheat
        );

        assertThatThrownBy(() -> {
            final String s = objectMapper.writeValueAsString(
                actionParamWrapper
            );
            System.out.println("s = " + s);
        })
            .isInstanceOf(JsonProcessingException.class)
            .hasMessage("...");
    }

    @Test
    void canDeserValidActionParam() throws JsonProcessingException {
        final String json = "{\"type\": \"play\", \"cards\": [\"RA\", \"B2\"]}";
        assertThat(
            objectMapper.readValue(json, ActionParam.class)
        ).isInstanceOfSatisfying(PlayerPlaysParam.class, play -> {
            assertThat(play.cards()).containsExactlyInAnyOrder(
                DeckConstants.RA,
                DeckConstants.B2
            );
        });
    }

    @ParameterizedTest
    @MethodSource
    void rejectDeserOfUnregisteredActionParamType(String json) {
        assertThatThrownBy(() -> objectMapper.readValue(json, ActionParam.class)
        )
            .isInstanceOf(InvalidTypeIdException.class)
            .hasMessageContaining("Could not resolve type id");
    }

    static Stream<Arguments> rejectDeserOfUnregisteredActionParamType() {
        return Stream.of(
            arguments(
                "{\"type\": \"cheatDeal\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"
            ),
            arguments(
                "{\"type\": \"ImmutableCheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"
            ),
            arguments(
                "{\"type\": \"CheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"
            ),
            arguments(
                "{\"type\": \"net.incongru.tichu.action.param.ImmutableCheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"
            ),
            arguments(
                "{\"type\": \"net.incongru.tichu.action.param.CheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"
            )
        );
    }

    enum KebabTests {
        lol,
        LOL,
        Lol,
        lol_what,
        LOL_WHAT,
        Lol_What,
        _a,
        a_,
    }

    @Test
    void kebabWorksAsIntended() {
        assertThat(kebab(KebabTests.lol)).isEqualTo("lol");
        assertThat(kebab(KebabTests.LOL)).isEqualTo("lol");
        assertThat(kebab(KebabTests.Lol)).isEqualTo("lol");
        assertThat(kebab(KebabTests.lol_what)).isEqualTo("lol-what");
        assertThat(kebab(KebabTests.LOL_WHAT)).isEqualTo("lol-what");
        assertThat(kebab(KebabTests.Lol_What)).isEqualTo("lol-what");
        assertThat(kebab(KebabTests._a)).isEqualTo("-a");
        assertThat(kebab(KebabTests.a_)).isEqualTo("a-");
    }

    static class CardArrayWrapper {

        @JsonProperty
        Card[] cards;
    }

    static class UserIdWrapper {

        @JsonProperty
        UserId user;
    }

    static class ActionParamWrapper {

        @JsonProperty
        final ActionParam action;

        @JsonCreator
        ActionParamWrapper(ActionParam action) {
            this.action = action;
        }
    }
}
