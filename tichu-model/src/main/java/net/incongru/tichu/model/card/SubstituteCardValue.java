package net.incongru.tichu.model.card;

import static net.incongru.tichu.model.card.CardSpecials.Phoenix;

/**
 * This really just helps supporting Phoenix where it is used as a substitute for another card
 * in a straight.
 *
 * @see net.incongru.tichu.model.plays.Straight
 */
public record SubstituteCardValue(CardValue sub) implements CardValue {
    // Convenience factory method for Straight, where we "calculate" a playOrder rather than get it from a card
    public static CardValue substituteFor(int playOrder) {
        return new SubstituteCardValue(CardNumbers.byPlayOrder(playOrder));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public int scoreValue() {
        return Phoenix.scoreValue();
    }

    @Override
    public int playOrder() {
        return sub.playOrder();
    }

    @Override
    public String niceName() {
        return sub.niceName();
    }

    @Override
    public char shortName() {
        return (char) ('0' + sub.playOrder());
    }
}
