package net.incongru.tichu.model.plays;

import java.util.Collections;
import java.util.Set;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Trick;

/**
 * @author gjoseph
 */
public abstract class AbstractPlay<P extends Trick.Play> implements Trick.Play<P> {
    private final Set<Card> cards;

    public AbstractPlay(Set<Card> cards) {
        this.cards = cards;
    }

    @Override
    public Set<Card> getCards() {
        return cards;
    }

    @Override
    public boolean isBomb() {
        return false;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    protected Card smallestCard() {
        return Collections.min(cards, Card.Comparators.BY_PLAY_ORDER);
    }

    @Override
    public String toString() {
        return describe();
    }
}
