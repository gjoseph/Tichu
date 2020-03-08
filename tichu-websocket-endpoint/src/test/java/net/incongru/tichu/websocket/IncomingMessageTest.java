package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
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

class IncomingMessageTest {

    @ParameterizedTest
    @MethodSource
    <T> void canDecodeSubTypesOfIncomingMessage(String json, Class<T> expectedParsedType, Consumer<T> assertionsOnResult) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final IncomingMessage parsed = objectMapper.readValue(json, IncomingMessage.class);
        System.out.println("parsed = " + parsed);
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
    void canEncodeSubtypesOfMessages(Object toEncode, String expectedJson) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        assertThatJson(objectMapper.writeValueAsString(toEncode)).isEqualTo(expectedJson);
    }

    static Stream<Arguments> canEncodeSubtypesOfMessages() {
        return Stream.of(
                arguments(ImmutableIncomingChatMessage.builder().content("hello").build(), "{type:'chat', content:'hello'}"),
                arguments(ImmutableOtherThing.builder().thing("yayaya").build(), "{type:'other', thing:'yayaya'}")
        );
    }

    @Test
    void jacksonMappingIsSymetric() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        final String input = "{\"type\":\"chat\", \"content\":\"HELLO\"}";
        final IncomingMessage parsed = objectMapper.readValue(input, IncomingMessage.class);
        final String rewritten = objectMapper.writeValueAsString(parsed);
        assertThatJson(input).isEqualTo(rewritten);

        final ImmutableOtherThing initialObj = ImmutableOtherThing.builder().thing("yayaya").build();
        final String serialised = objectMapper.writeValueAsString(initialObj);
        final IncomingMessage deserialised = objectMapper.readValue(serialised, IncomingMessage.class);
        assertEquals(initialObj, deserialised);
    }

}