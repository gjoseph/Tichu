package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;

class PlayerIsReady implements Action {
    private final String playerName;

    PlayerIsReady(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public Result exec() {
        throw new IllegalStateException("Not implemented yet");
    }
}
