package net.incongru.tichu.websocket;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionResponse;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@Value.Style(stagedBuilder = true)
@JsonSerialize(as = ImmutableGameActionResultMessage.class)
@JsonDeserialize(as = ImmutableGameActionResultMessage.class)
public interface GameActionResultMessage extends OutgoingMessage {

    /**
     * {@link GameActionResultMessage}s always have a transaction ID, copied from the message they are a response to.
     * It must only be unique enough for the client to identify responses generated against this message.
     * TODO - another type of OutgoingMessage for game results for non-response messages (i.e broadcasts to other players)
     */
    @Nonnull
    @JsonProperty("txId")
    String clientTxId();

    @JsonUnwrapped
    ActionResponse result();
}
