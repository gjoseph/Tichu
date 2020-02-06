package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.GameContext;

class PlayerPasses implements Action {
    private final String playerName;

    PlayerPasses(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public Result exec(GameContext ctx) {
        throw new IllegalStateException("Not implemented yet");
    }
}
