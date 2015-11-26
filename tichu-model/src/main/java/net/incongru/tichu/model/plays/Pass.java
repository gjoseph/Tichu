package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;

import java.util.Collections;
import java.util.Set;

/**
 * Pass _is_ a {@link Play}
 */
public class Pass extends AbstractPlay<Pass> {
    public Pass() {
        super(Collections.emptySet());
    }

    @Override
    public String describe() {
        return "Passed turn";
    }

    @Override
    public boolean canBePlayedAfter(Play other) {
        // One can always pass
        return true;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(Pass other) {
        // Bypassed by reimplementing canBePlayedAfter
        throw new IllegalStateException("This should not be called");
    }

    public static class Factory implements PlayFactory<Pass> {
        @Override
        public Pass is(Set<Card> cards) {
            return cards.isEmpty() ? new Pass() : null;
        }
    }
}
