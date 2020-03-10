package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Player;

import java.util.Set;

class CheatDeal implements Action {
    private final String playerName;
    private final Set<Card> cards;

    CheatDeal(String playerName, Set<Card> cards) {
        // TODO check we're in cheat/simu mode
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public Result exec(GameContext ctx) {
        if (ctx.game().isStarted()) {
            throw new IllegalStateException("Game is already started");
        }
        if (ctx.game().isReadyToStart()) {
            throw new IllegalStateException("Game is ready to start, too late to cheat");
        }
        final Player player = ctx.player(playerName);
        cards.forEach(c -> player.deal(c));

        return new Success() {
        };
    }

}
