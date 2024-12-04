package net.incongru.tichu.websocket;

import java.util.Collection;
import java.util.List;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.card.Card;

public record GameStatusMessage(
    Collection<PlayerStatus> players,
    // TODO naming? In PlayerPlaysResponse this is called nextPlayer
    UserId currentPlayer,
    // current play type TODO not a String
    String play,
    List<Card> playedCards
)
    implements OutgoingMessage {
    record PlayerStatus(
        UserId id,
        PlayerState status,
        int team,
        int cardsInHand,
        int cardsCollected
    ) {}

    enum PlayerState {
        READY,
        NOT_READY,
    }
}
