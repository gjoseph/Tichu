package net.incongru.tichu.websocket;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionResponse;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableGameActionResultMessage.class)
@JsonDeserialize(as = ImmutableGameActionResultMessage.class)
public interface GameActionResultMessage extends OutgoingMessage {

    @JsonUnwrapped
    ActionResponse result();
}
