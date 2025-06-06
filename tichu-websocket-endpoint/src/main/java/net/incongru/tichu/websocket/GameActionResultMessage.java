package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import net.incongru.tichu.action.ActionResponse;
import org.jspecify.annotations.NonNull;

/**
 * {@link GameActionResultMessage}s always have a transaction ID, copied from the message they are a response to.
 * It must only be unique enough for the client to identify responses generated against this message.
 * TODO - another type of OutgoingMessage for game results for non-response messages (i.e broadcasts to other players)
 */
public record GameActionResultMessage(
    @NonNull @JsonProperty("txId") String clientTxId,
    @JsonUnwrapped ActionResponse result
)
    implements OutgoingMessage {}
