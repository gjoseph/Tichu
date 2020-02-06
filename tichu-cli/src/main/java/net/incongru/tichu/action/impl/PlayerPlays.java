package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.simu.GameContext;

import java.util.List;

class PlayerPlays implements Action {
    private final String playerName;
    private final List<Card> cards;

    PlayerPlays(String playerName, List<Card> cards) {
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public Result exec(GameContext ctx) {
        throw new IllegalStateException("Not implemented yet");
    }
}
