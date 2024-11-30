package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.card.Card;

import java.util.Set;
import net.incongru.tichu.model.Card;

/**
 *
 */
public class FullHouse extends AbstractPlay<FullHouse> {

    private FullHouse(Set<Card> set) {
        super(set);
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(FullHouse other) {
        throw new IllegalStateException("Not implemented yet!"); // TODO
    }

    @Override
    public String describe() {
        throw new IllegalStateException("Not implemented yet!"); // TODO
    }
}
