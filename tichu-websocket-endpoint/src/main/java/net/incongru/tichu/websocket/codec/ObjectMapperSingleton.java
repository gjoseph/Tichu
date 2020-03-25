package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.incongru.tichu.model.Card;

class ObjectMapperSingleton {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new Jdk8Module());

        MAPPER.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // ?? maybe ignore unknown?
        // who cares MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.addMixIn(Card.class, CardJacksonSupport.class); // could register serialiser/deserialiser directly instead
    }

    private ObjectMapperSingleton() {
    }

    static ObjectMapper get() {
        return MAPPER;
    }
}