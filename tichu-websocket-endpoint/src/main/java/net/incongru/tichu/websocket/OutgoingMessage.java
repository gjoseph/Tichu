package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        // our json shapes will have a "type" with values as specified below
        property = "type"
)
@JsonSubTypes({
        // We specify the type here using the Immutable* impls so serialising uses the correct name
        // but we still need to also specify @JsonDeserialize(as) on the abstract types for deserialisation
        @JsonSubTypes.Type(value = ImmutableOutgoingChatMessage.class, name = "chat"),
        @JsonSubTypes.Type(value = ImmutableGameActionResultMessage.class, name = "game")
})
public interface OutgoingMessage {

}