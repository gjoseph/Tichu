package net.incongru.tichu.cli;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Players;

import java.util.function.Function;

/**
 * Some bullshit ex-groovy code that was trying to build a DSL but really was just cobbled together until something kinda
 * worked, but never really did. Converted to Java until we can properly rewrite/ditch.
 */
class ActingPlayer {

    private final TichuDSL ctx;
    private final Players.Player p;

    ActingPlayer(TichuDSL ctx, Players.Player p) {
        this.ctx = ctx;
        this.p = p;
    }

    public Function<Players.Player, Void> plays(Card... cards) {
        return ctx.playerPlayCards(cards);
    }

}
