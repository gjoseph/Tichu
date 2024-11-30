package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardComparators;

import java.util.Collections;
import java.util.Set;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;

/**
 *
 */
public abstract class AbstractPlay<P extends Play> implements Play<P> {

    private final Set<Card> cards;

    protected AbstractPlay(Set<Card> cards) {
        this.cards = Collections.unmodifiableSet(cards);
    }

    @Override
    public Set<Card> getCards() {
        return cards;
    }

    @Override
    public boolean canBePlayedAfter(Play other) {
        // Anything goes as first play
        return (
            other == Initial.INSTANCE ||
            (getClass().equals(other.getClass()) &&
                canBePlayedAfterTypeSafe((P) other))
        );
    }

    protected abstract boolean canBePlayedAfterTypeSafe(P other);

    @Override
    public boolean isBomb() {
        return false;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    protected Card smallestCard() {
        return Collections.min(cards, CardComparators.BY_PLAY_ORDER);
    }

    @Override
    public String toString() {
        return describe();
    }
}
