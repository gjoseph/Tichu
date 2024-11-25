package net.incongru.tichu.model;


import javax.annotation.Nullable;

public record Played(
        // can be null for initial
        @Nullable Player player,
        Play play) {
}
