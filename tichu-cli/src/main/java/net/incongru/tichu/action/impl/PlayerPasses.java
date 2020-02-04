package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;

class PlayerPasses implements Action {
    private final String playerName;

    PlayerPasses(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public Result exec() {
        throw new IllegalStateException("Not implemented yet");
    }
}
