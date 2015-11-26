package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Card;

import java.util.Set;

/**
 * @author gjoseph
 */
public class BombOf4 extends NSameValue<BombOf4> {
    public BombOf4(Set<Card> cards, Card.CardValue value) {
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
        protected BombOf4 newPlay(Set<Card> cards, Card.CardValue value) {
            return new BombOf4(cards, value);
        }
    }

}
