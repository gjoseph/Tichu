package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Card;

import java.util.Set;

/**
 *
 */
public class Triple extends NSameValue<Triple> {
    private Triple(Set<Card> cards, Card.CardValue value) {
        super(cards, value);
    }

    public static class Factory extends NSameValuesFactory<Triple> {
        @Override
        protected int expectedSize() {
            return 3;
        }

        @Override
        protected Triple newPlay(Set<Card> cards, Card.CardValue value) {
            return new Triple(cards, value);
        }
    }
}
