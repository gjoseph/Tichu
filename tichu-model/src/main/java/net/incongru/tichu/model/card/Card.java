package net.incongru.tichu.model.card;

/**
 *
 */
public interface Card {
    CardValue val();

    CardSuit suit();

    String name();

    String shortName();

    static Card of(CardSpecials val) {
        return new CardImpl(val, null);
    }

    static Card of(CardNumbers val, CardSuit suit) {
        return new CardImpl(val, suit);
    }
}
