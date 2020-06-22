package net.incongru.tichu.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.Player;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@Value.Style(stagedBuilder = true)
@JsonSerialize(as = ImmutablePlayerHandMessage.class)
@JsonDeserialize(as = ImmutablePlayerHandMessage.class)
public interface PlayerHandMessage extends OutgoingMessage {

    // Unclear if this will be useful since
    // a) this txId is currently the one of the last player to be ready
    // b) it already had a response to the game action that originated it
    @Nonnull
    @JsonProperty("txId")
    String clientTxId();

    Player.Hand hand();
}
