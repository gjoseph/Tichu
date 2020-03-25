package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectMapperSingletonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = ObjectMapperSingleton.get();
    }

    @Test
    void canSerCards() throws JsonProcessingException {
        final Card[] cards = {DeckConstants.Pagoda_4, DeckConstants.MahJong};

        assertThatJson(objectMapper.writeValueAsString(cards)).isEqualTo("['B4', '*1']");
    }

    @Test
    void canDeserCards() throws JsonProcessingException {
        final String json = "{\"cards\":[\"jade_ace\", \"*d\"]}";
        final ArrayWrapper readCards = objectMapper.readValue(json, ArrayWrapper.class);
        assertThat(readCards.cards).containsExactlyInAnyOrder(DeckConstants.Jade_Ace, DeckConstants.Dragon);
    }

    @Test
    void deserUnknownCardResultsInJacksonException() {
        assertThatThrownBy(() -> objectMapper.readValue("\"foo\"", Card.class))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("foo is not a valid card name");
    }

    static class ArrayWrapper {
        @JsonProperty
        Card[] cards;
    }

}