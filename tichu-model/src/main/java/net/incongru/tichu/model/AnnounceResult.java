package net.incongru.tichu.model;

import net.incongru.tichu.model.immu.Tuple;
import org.immutables.value.Value.Immutable;

/**
 * After play, the announce was met or failed.
 */
@Immutable
@Tuple
interface AnnounceResult {
    Players.Player player();

    Announce announce();

    Boolean result();
}
