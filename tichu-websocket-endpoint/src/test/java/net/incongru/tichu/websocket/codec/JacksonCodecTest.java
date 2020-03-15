package net.incongru.tichu.websocket.codec;

import net.incongru.tichu.websocket.ChatEndpoint;
import net.incongru.tichu.websocket.ImmutableIncomingChatMessage;
import net.incongru.tichu.websocket.ImmutableOtherThing;
import net.incongru.tichu.websocket.IncomingChatMessage;
import net.incongru.tichu.websocket.IncomingMessage;
import net.incongru.tichu.websocket.OtherThing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JacksonCodecTest {

    @ParameterizedTest
    @MethodSource
    <T> void canDecodeSubTypesOfIncomingMessage(String json, Class<T> expectedParsedType, Consumer<T> assertionsOnResult) throws Exception {
        final JacksonCodec<IncomingMessage> codec = new ChatEndpoint.IncomingMessageCodec();
        final IncomingMessage parsed = codec.decode(json);
        assertThat(parsed).isInstanceOfSatisfying(expectedParsedType, assertionsOnResult);
    }

    static Stream<Arguments> canDecodeSubTypesOfIncomingMessage() {
        return Stream.of(
                arguments("{\"type\":\"chat\", \"content\":\"HELLO\"}", IncomingChatMessage.class,
                        (Consumer<IncomingChatMessage>) m -> assertThat(m.content()).isEqualTo("HELLO")),
                arguments("{\"type\":\"other\", \"thing\":\"yayaya\"}", OtherThing.class,
                        (Consumer<OtherThing>) m -> assertThat(m.thing()).isEqualTo("yayaya"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void canEncodeSubtypesOfMessages(IncomingMessage toEncode, String expectedJson) throws Exception {
        final JacksonCodec<IncomingMessage> codec = new ChatEndpoint.IncomingMessageCodec();
        assertThatJson(codec.encode(toEncode)).isEqualTo(expectedJson);
    }

    static Stream<Arguments> canEncodeSubtypesOfMessages() {
        return Stream.of(
                arguments(ImmutableIncomingChatMessage.builder().content("hello").build(), "{type:'chat', content:'hello'}"),
                arguments(ImmutableOtherThing.builder().thing("yayaya").build(), "{type:'other', thing:'yayaya'}")
        );
    }

    @Test
    void jacksonMappingIsSymetric() throws Exception {
        final JacksonCodec<IncomingMessage> codec = new ChatEndpoint.IncomingMessageCodec();

        final String input = "{\"type\":\"chat\", \"content\":\"HELLO\"}";
        final IncomingMessage parsed = codec.decode(input);
        final String rewritten = codec.encode(parsed);
        assertThatJson(input).isEqualTo(rewritten);

        final ImmutableOtherThing initialObj = ImmutableOtherThing.builder().thing("yayaya").build();
        final String serialised = codec.encode(initialObj);
        final IncomingMessage deserialised = codec.decode(serialised);
        assertEquals(initialObj, deserialised);
    }

}