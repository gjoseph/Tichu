package net.incongru.tichu.server.resource;

import java.util.UUID;

public record GameId(UUID uuid) {
    public static GameId newRandomId() {
        return new GameId(UUID.randomUUID());
    }
}
