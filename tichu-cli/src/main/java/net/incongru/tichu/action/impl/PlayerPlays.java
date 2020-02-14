package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.simu.SimulatedGameContext;

import java.util.Set;

class PlayerPlays implements Action {
    private final String playerName;
    private final Set<Card> cards;

    PlayerPlays(String playerName, Set<Card> cards) {
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public Result exec(SimulatedGameContext ctx) {
        throw new IllegalStateException("Not implemented yet");
    }
}
