package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.incongru.tichu.websocket.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {

    private final ObjectMapper mapper = ObjectMapperSingleton.get();

    @Override
    public void init(EndpointConfig ec) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Message decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, Message.class);
        } catch (IOException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}