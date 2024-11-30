package net.incongru.tichu.model.plays;

import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.card.Card;

import java.util.Collections;
import java.util.Set;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;

/**
 * Null-pattern implementation of {@link Play} which is used as the "first" element of a {@link net.incongru.tichu.model.Trick}.
 */
public final class Initial implements Play {

    public static Initial INSTANCE = new Initial();

    private Initial() {}

    @Override
    public Set<Card> getCards() {
        return Collections.emptySet();
    }

    @Override
    public boolean canBePlayedAfter(Play other) {
        return false;
    }

    @Override
    public boolean isBomb() {
        return false;
    }

    @Override
    public String name() {
        return "(start)";
    }

    @Override
    public String describe() {
        return "(Nothing has been played yet)";
    }
}
