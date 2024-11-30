package net.incongru.tichu.model.plays;

import java.util.Set;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardValue;

/**
 *
 */
public class Triple extends NSameValue<Triple> {

    private Triple(Set<Card> cards, CardValue value) {
        super(cards, value);
    }

    public static class Factory extends NSameValuesFactory<Triple> {

        @Override
        protected int expectedSize() {
            return 3;
        }

        @Override
        protected Triple newPlay(Set<Card> cards, CardValue value) {
            return new Triple(cards, value);
        }
    }
}
