package net.incongru.tichu.model.plays;

import java.util.Set;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardValue;

/**
 *
 */
public class Pair extends NSameValue<Pair> {

    private Pair(Set<Card> cards, CardValue value) {
        super(cards, value);
    }

    public static class Factory extends NSameValuesFactory<Pair> {

        @Override
        protected int expectedSize() {
            return 2;
        }

        @Override
        protected Pair newPlay(Set<Card> cards, CardValue value) {
            return new Pair(cards, value);
        }
    }
}
