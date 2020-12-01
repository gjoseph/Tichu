package net.incongru.tichu.websocket;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.List;

@Value.Immutable
@Value.Style(stagedBuilder = true)
@JsonSerialize(as = ImmutableGameStatusMessage.class)
@JsonDeserialize(as = ImmutableGameStatusMessage.class)
public interface GameStatusMessage extends OutgoingMessage {
    Collection<PlayerStatus> players();

    // TODO naming? In PlayerPlaysResponse this is called nextPlayer
    UserId currentPlayer();

    // current play type TODO not a String
    String play();

    List<Card> playedCards();

    @Value.Immutable
    @Value.Style(stagedBuilder = true)
    interface PlayerStatus {
        UserId id();

        PlayerState status();

        int team();

        int cardsInHand();

        int cardsCollected();
    }

    enum PlayerState {READY, NOT_READY}
}
