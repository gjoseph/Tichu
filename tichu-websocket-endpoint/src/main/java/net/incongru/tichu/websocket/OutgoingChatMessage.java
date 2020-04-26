package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(stagedBuilder = true, strictBuilder = true) // disables the "from" method in builder
@JsonSerialize(as = ImmutableOutgoingChatMessage.class)
@JsonDeserialize(as = ImmutableOutgoingChatMessage.class)
public interface OutgoingChatMessage extends OutgoingMessage {
    UserId from();

    String content();
}
