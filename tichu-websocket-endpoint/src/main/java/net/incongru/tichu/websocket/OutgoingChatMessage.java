package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.incongru.tichu.model.UserId;
import org.jspecify.annotations.NonNull;

/**
 * {@link OutgoingChatMessage}s always have a transaction ID, copied from the message they are re-broadcasting.
 * It must only be unique enough for the client to identify responses generated against this message.
 * TODO - another type of OutgoingMessage for game results for non-response messages? (i.e broadcasts to other players)
 */
public record OutgoingChatMessage(
    UserId from,
    String content,
    @NonNull @JsonProperty("txId") String clientTxId
)
    implements OutgoingMessage {}
