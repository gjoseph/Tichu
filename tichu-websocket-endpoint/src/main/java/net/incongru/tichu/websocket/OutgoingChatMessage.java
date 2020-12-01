package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@Value.Style(stagedBuilder = true, strictBuilder = true) // disables the "from" method in builder
@JsonSerialize(as = ImmutableOutgoingChatMessage.class)
@JsonDeserialize(as = ImmutableOutgoingChatMessage.class)
public interface OutgoingChatMessage extends OutgoingMessage {
    UserId from();

    String content();

    /**
     * {@link OutgoingChatMessage}s always have a transaction ID, copied from the message they are re-broadcasting.
     * It must only be unique enough for the client to identify responses generated against this message.
     * TODO - another type of OutgoingMessage for game results for non-response messages? (i.e broadcasts to other players)
     */
    @Nonnull
    @JsonProperty("txId")
    String clientTxId();
}
