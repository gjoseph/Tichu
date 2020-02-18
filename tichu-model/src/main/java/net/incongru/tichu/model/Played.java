package net.incongru.tichu.model;

import net.incongru.tichu.model.immu.Tuple;
import org.immutables.value.Value.Immutable;

import javax.annotation.Nullable;

@Immutable
@Tuple
public interface Played {
    @Nullable // can be null for initial
    Player player();

    Play play();
}
