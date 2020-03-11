package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.ActionResult.ErrorPlayResult;
import net.incongru.tichu.action.ActionResult.SuccessPlayResult;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;

import java.util.Set;

class PlayerPlays implements Action<PlayerPlays.PlayerPlaysParam> {

    PlayerPlays() {
    }

    @Override
    public Class<PlayerPlaysParam> paramType() {
        return PlayerPlaysParam.class;
    }

    @Override
    public ActionResult exec(GameContext ctx, PlayerPlaysParam param) {
        final Player player = ctx.player(param.playerName);
        final Play.PlayResult res = ctx.game().currentRound().currentTrick().play(player, param.cards);

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

    static class PlayerPlaysParam implements ActionParam {
        private final String playerName;
        private final Set<Card> cards;
        // TODO Immutables and jackson mapping

        public PlayerPlaysParam(String playerName, Set<Card> cards) {
            this.playerName = playerName;
            this.cards = cards;
        }
    }
}
