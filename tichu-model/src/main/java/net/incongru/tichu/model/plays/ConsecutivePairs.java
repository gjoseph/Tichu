package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Card;

import java.util.List;
import java.util.Set;

/**
 */
public class ConsecutivePairs extends AbstractPlay<ConsecutivePairs> {
    private ConsecutivePairs(Set<Card> cards) {
        super(cards);
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(ConsecutivePairs other) {
        throw new IllegalStateException("Not implemented yet!"); // TODO
    }

    @Override
    public String describe() {
        throw new IllegalStateException("Not implemented yet!"); // TODO
    }

    public static class Factory implements PlayFactory<ConsecutivePairs> {
        @Override
        public ConsecutivePairs is(Set<Card> cards) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
