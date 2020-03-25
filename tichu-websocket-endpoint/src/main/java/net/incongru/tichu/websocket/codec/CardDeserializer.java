package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;

import java.io.IOException;

public class CardDeserializer extends FromStringDeserializer<Card> {// StdScalarDeserializer<Card> {

    public CardDeserializer() {
        super(Card.class);
    }

    @Override
    protected Card _deserialize(String value, DeserializationContext ctxt) throws IOException {
        return DeckConstants.byName(value);
    }
}
