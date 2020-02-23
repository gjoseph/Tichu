package net.incongru.tichu.model;

import net.incongru.tichu.model.immu.Tuple;
import org.immutables.value.Value;

/**
 *
 */
@Tuple
@Value.Immutable
public abstract class Score {
    abstract int getTeam1();

    abstract int getTeam2();

    @Override
    public String toString() {
        return String.format("%d:%d", getTeam1(), getTeam2());
    }
}
