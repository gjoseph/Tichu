package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.incongru.tichu.model.Player;
import org.jspecify.annotations.NonNull;

public record PlayerHandMessage(
    // Unclear if this will be useful since
    // a) this txId is currently the one of the last player to be ready
    // b) it already had a response to the game action that originated it
    @NonNull @JsonProperty("txId") String clientTxId,

    Player.Hand hand
) implements OutgoingMessage {}
