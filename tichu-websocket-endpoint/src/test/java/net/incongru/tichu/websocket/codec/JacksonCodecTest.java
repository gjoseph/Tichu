package net.incongru.tichu.websocket.codec;

import net.incongru.tichu.action.param.ImmutablePlayerPlaysParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.util.DeckConstants;
import net.incongru.tichu.websocket.GameActionMessage;
import net.incongru.tichu.websocket.ImmutableGameActionMessage;
import net.incongru.tichu.websocket.ImmutableGameActionResultMessage;
import net.incongru.tichu.websocket.ImmutableOutgoingChatMessage;
import net.incongru.tichu.websocket.IncomingChatMessage;
import net.incongru.tichu.websocket.IncomingMessage;
import net.incongru.tichu.websocket.OutgoingMessage;
import net.incongru.tichu.websocket.RoomEndpoint;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JacksonCodecTest {

    @ParameterizedTest
    @MethodSource
    <T> void canDecodeSubTypesOfIncomingMessages(String json, Class<T> expectedParsedType, Consumer<T> assertionsOnResult) throws Exception {
        final JacksonCodec<IncomingMessage> codec = new RoomEndpoint.IncomingMessageCodec();
        final IncomingMessage parsed = codec.decode(json);
        assertThat(parsed).isInstanceOfSatisfying(expectedParsedType, assertionsOnResult);
    }

    static Stream<Arguments> canDecodeSubTypesOfIncomingMessages() {
        return Stream.of(
                arguments("{\"type\":\"chat\", \"content\":\"HELLO\"}", IncomingChatMessage.class,
                        (Consumer<IncomingChatMessage>) m -> assertThat(m.content()).isEqualTo("HELLO")),
                arguments("{\"type\":\"game\", \"action\": {\"type\":\"play\", \"cards\": [\"star_ace\", \"sword_ace\"]}}", GameActionMessage.class,
                        (Consumer<GameActionMessage>) m -> assertThat(m.action()).isEqualTo(sampleGameParam()))
        );
    }

    @ParameterizedTest
    @MethodSource
    void canEncodeSubtypesOfOutgoingMessages(OutgoingMessage toEncode, String expectedJson) throws Exception {
        final JacksonCodec<OutgoingMessage> codec = new RoomEndpoint.OutgoingMessageCodec();
        assertThatJson(codec.encode(toEncode))
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedJson);
    }

    static Stream<Arguments> canEncodeSubtypesOfOutgoingMessages() {
        return Stream.of(
                arguments(ImmutableOutgoingChatMessage.builder().from(UserId.of("dummy")).content("hello").build(),
                        "{type:'chat', from: 'dummy', content:'hello'}"),
                arguments(ImmutableGameActionResultMessage.builder().player(UserId.of("dummy")).result("temporary action result").build(),
                        "{" +
                        "  type: 'game'," +
                        "  player: 'dummy'," +
                        "  result: 'temporary action result'" +
                        " }")
        );
    }

    @Test
    void jacksonMappingIsSymetric() throws Exception {
        final JacksonCodec<IncomingMessage> codec = new RoomEndpoint.IncomingMessageCodec();

        final String input = "{\"type\":\"chat\", \"content\":\"HELLO\"}";
        final IncomingMessage parsed = codec.decode(input);
        final String rewritten = codec.encode(parsed);
        assertThatJson(input).isEqualTo(rewritten);

        final ImmutableGameActionMessage initialObj = ImmutableGameActionMessage.builder().action(sampleGameParam()).build();
        final String serialised = codec.encode(initialObj);
        final IncomingMessage deserialised = codec.decode(serialised);
        assertEquals(initialObj, deserialised);
    }

    private static PlayerPlaysParam sampleGameParam() {
        return ImmutablePlayerPlaysParam.builder().cards(Set.of(DeckConstants.Star_Ace, DeckConstants.Sword_Ace)).build();
    }
}
