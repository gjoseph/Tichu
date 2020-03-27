package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.databind.ObjectMapper;

class ObjectMapperSingleton {

    private static final ObjectMapper mapper;

    static {
        mapper = JacksonSetup.setupJacksonMapper();
    }

    private ObjectMapperSingleton() {
    }

    static ObjectMapper get() {
        return mapper;
    }


}