package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CardDeserializer.class)
abstract class CardJacksonSupport {

//    @JsonCreator -- does not work, so we use a Serializer instead
//    static Card byName(String cardName) {
//        return DeckConstants.byName(cardName);
//    }

    @JsonValue
    abstract String shortName();


}
