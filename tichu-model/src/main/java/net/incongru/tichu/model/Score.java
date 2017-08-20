package net.incongru.tichu.model;

import net.incongru.tichu.model.immu.Tuple;
import org.immutables.value.Value;

/**
 *
 */
@Tuple
@Value.Immutable
public interface Score {
    int getTeam1();

    int getTeam2();
}
