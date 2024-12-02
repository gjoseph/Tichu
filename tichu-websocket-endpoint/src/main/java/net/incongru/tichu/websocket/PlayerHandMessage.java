package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnull;
import net.incongru.tichu.model.Player;

public record PlayerHandMessage(
    // Unclear if this will be useful since
    // a) this txId is currently the one of the last player to be ready
    // b) it already had a response to the game action that originated it
    @Nonnull @JsonProperty("txId") String clientTxId,

    Player.Hand hand
)
    implements OutgoingMessage {}
