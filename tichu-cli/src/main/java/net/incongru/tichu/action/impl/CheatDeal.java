package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.simu.SimulatedGameContext;

import java.util.List;

class CheatDeal implements Action {
    private final String playerName;
    private final List<Card> cards;

    CheatDeal(String playerName, List<Card> cards) {
        // TODO check we're in cheat/simu mode
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public Result exec(SimulatedGameContext ctx) {
        throw new IllegalStateException("Not implemented yet");
    }
}
