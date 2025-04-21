package net.incongru.tichu.model;

import org.jspecify.annotations.Nullable;

public record Played(
    // can be null for initial
    @Nullable Player player,
    Play play
) {}
