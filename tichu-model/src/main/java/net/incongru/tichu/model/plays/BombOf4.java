package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardValue;

import java.util.Set;
import net.incongru.tichu.model.Card;

/**
 *
 */
public class BombOf4 extends NSameValue<BombOf4> {
    private BombOf4(Set<Card> cards, CardValue value) {
        super(cards, value);
    }

    @Override
    public boolean isBomb() {
        return true;
    }

    public static class Factory extends NSameValuesFactory<BombOf4> {

        @Override
        protected int expectedSize() {
            return 4;
        }

        @Override
        protected BombOf4 newPlay(Set<Card> cards, CardValue value) {
            return new BombOf4(cards, value);
        }
    }
}
