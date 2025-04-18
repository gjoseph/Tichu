package net.incongru.tichu.model.plays;

import java.util.Set;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.card.Card;

/**
 * Null-pattern implementation for plays that are not valid.
 */
public final class InvalidPlay extends AbstractPlay {

    public InvalidPlay(Set<Card> cards) {
        super(cards);
    }

    @Override
    public boolean canBePlayedAfter(Play other) {
        return false;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(Play other) {
        // This is called by super.canBePlayedAfter(), which we overrode above
        throw new IllegalStateException("Should not be called");
    }

    @Override
    public boolean isBomb() {
        return false;
    }

    @Override
    public String describe() {
        return "This does not correspond to any known trick: " + getCards();
    }
}
