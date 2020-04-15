package net.incongru.tichu.websocket;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableGameActionResultMessage.class)
@JsonDeserialize(as = ImmutableGameActionResultMessage.class)
public interface GameActionResultMessage extends OutgoingMessage {
    UserId player();

    // maybe inline this?
    ActionResponse result();
}
