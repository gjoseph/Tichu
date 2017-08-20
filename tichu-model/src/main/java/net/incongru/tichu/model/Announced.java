package net.incongru.tichu.model;

import net.incongru.tichu.model.immu.Tuple;
import org.immutables.value.Value.Immutable;

/**
 * During play, the announce is made and pending.
 */
@Immutable
@Tuple
interface Announced {
    Players.Player player();

    Announce announce();
}
