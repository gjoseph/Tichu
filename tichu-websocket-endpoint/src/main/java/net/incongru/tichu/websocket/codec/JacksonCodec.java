package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Using technique described at https://dzone.com/articles/using-java-websockets-jsr-356,
 * each bean we need will need to subclass this to specify type
 * TODO switch to TextStream or Binary?
 */
public abstract class JacksonCodec<T> implements Encoder.Text<T>, Decoder.Text<T> {

    private final ObjectMapper mapper = ObjectMapperSingleton.get();
    private final Class<T> type;

    public JacksonCodec() {
        final ParameterizedType thisClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        final Type t = thisClass.getActualTypeArguments()[0];
        if (t instanceof Class) {
            this.type = (Class<T>) t;
        } else if (t instanceof ParameterizedType) {
            // Not sure this is the case
            this.type = (Class<T>) ((ParameterizedType) t).getRawType();
        } else {
            throw new IllegalStateException("Can't determine type for " + this);
        }
    }

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(T t) throws EncodeException {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new EncodeException(t, e.getMessage(), e);
        }
    }

    @Override
    public T decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, type);
        } catch (IOException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}