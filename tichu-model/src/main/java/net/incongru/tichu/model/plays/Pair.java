package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Card;

import java.util.Set;

/**
 */
public class Pair extends NSameValue<Pair> {

    public Pair(Set<Card> cards, Card.CardValue value) {
        super(cards, value);
    }

    public static class Factory extends NSameValuesFactory<Pair> {

        @Override
        protected int expectedSize() {
            return 2;
        }

        @Override
        protected Pair newPlay(Set<Card> cards, Card.CardValue value) {
            return new Pair(cards, value);
        }
    }

}
