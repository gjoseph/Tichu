package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.ImmutableCheatDealParam;
import net.incongru.tichu.action.param.ImmutablePlayerPlaysParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JacksonSetupTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = JacksonSetup.setupJacksonMapper();
    }

    @Test
    void canSerCards() throws JsonProcessingException {
        final Card[] cards = {DeckConstants.Pagoda_4, DeckConstants.MahJong};

        assertThatJson(objectMapper.writeValueAsString(cards)).isEqualTo("['B4', '*1']");
    }

    @Test
    void canDeserCards() throws JsonProcessingException {
        final String json = "{\"cards\":[\"jade_ace\", \"*d\"]}";
        final CardArrayWrapper readCards = objectMapper.readValue(json, CardArrayWrapper.class);
        assertThat(readCards.cards).containsExactlyInAnyOrder(DeckConstants.Jade_Ace, DeckConstants.Dragon);
    }

    @Test
    void deserUnknownCardResultsInJacksonException() {
        assertThatThrownBy(() -> objectMapper.readValue("\"foo\"", Card.class))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("foo is not a valid card name");
    }

    @Test
    void canSerValidActionParam() throws JsonProcessingException {
        final PlayerPlaysParam play = ImmutablePlayerPlaysParam.builder().cards(Set.of(DeckConstants.Star_Ace, DeckConstants.B2)).build();
        final ActionParamWrapper actionParamWrapper = new ActionParamWrapper(play);
        assertThatJson(objectMapper.writeValueAsString(actionParamWrapper))
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo("{action: {type: 'play', cards: ['RA', 'B2']}}");
    }

    @Test
    @Disabled("https://github.com/FasterXML/jackson-databind/issues/436")
    void rejectSerOfUnregisteredActionParamType() {
        final ActionParam cheat = ImmutableCheatDealParam.builder().cards(Set.of(DeckConstants.Star_Ace, DeckConstants.B2)).build();
        final ActionParamWrapper actionParamWrapper = new ActionParamWrapper(cheat);

        assertThatThrownBy(() -> {
            final String s = objectMapper.writeValueAsString(actionParamWrapper);
            System.out.println("s = " + s);
        })
                .isInstanceOf(JsonProcessingException.class)
                .hasMessage("...");
    }

    @Test
    void canDeserValidActionParam() throws JsonProcessingException {
        final String json = "{\"type\": \"play\", \"cards\": [\"RA\", \"B2\"]}";
        assertThat(objectMapper.readValue(json, ActionParam.class))
                .isInstanceOfSatisfying(PlayerPlaysParam.class, play -> {
                    assertThat(play.cards()).containsExactlyInAnyOrder(DeckConstants.RA, DeckConstants.B2);
                });
    }

    @ParameterizedTest
    @MethodSource
    void rejectDeserOfUnregisteredActionParamType(String json) {
        assertThatThrownBy(() -> objectMapper.readValue(json, ActionParam.class))
                .isInstanceOf(InvalidTypeIdException.class)
                .hasMessageContaining("Could not resolve type id");
    }

    static Stream<Arguments> rejectDeserOfUnregisteredActionParamType() {
        return Stream.of(
                arguments("{\"type\": \"cheatDeal\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"),
                arguments("{\"type\": \"ImmutableCheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"),
                arguments("{\"type\": \"CheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"),
                arguments("{\"type\": \"net.incongru.tichu.action.param.ImmutableCheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}"),
                arguments("{\"type\": \"net.incongru.tichu.action.param.CheatDealParam\", \"playerName\": \"charlie\", \"cards\": [\"RA\", \"B2\"]}")
        );
    }

    static class CardArrayWrapper {
        @JsonProperty
        Card[] cards;
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