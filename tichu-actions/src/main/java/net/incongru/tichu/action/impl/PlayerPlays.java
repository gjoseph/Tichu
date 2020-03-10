package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;

import java.util.Set;

class PlayerPlays implements Action {
    private final String playerName;
    private final Set<Card> cards;

    PlayerPlays(String playerName, Set<Card> cards) {
        this.playerName = playerName;
        this.cards = cards;
    }

    @Override
    public Result exec(GameContext ctx) {
        final Player player = ctx.player(playerName);
        final Play.PlayResult res = ctx.game().currentRound().currentTrick().play(player, cards);

        switch (res.result()) {
            case TRICK_END:
            case NEXTGOES:
                return new SuccessPlayResult(res);
            case TOOWEAK:
            case INVALIDPLAY:
            case INVALIDSTATE:
            case NOTINHAND:
                return new ErrorPlayResult(res);
            default:
                throw new IllegalStateException("Unknown result type :" + res.result());
        }
    }

}
