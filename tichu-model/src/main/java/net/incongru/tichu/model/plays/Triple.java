package net.incongru.tichu.model.plays;

import java.util.Set;

import net.incongru.tichu.model.Card;

/**
 * @author gjoseph
 */
public class Triple extends NSameValue<Triple> {
    public Triple(Set<Card> cards, Card.CardValue value) {
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
