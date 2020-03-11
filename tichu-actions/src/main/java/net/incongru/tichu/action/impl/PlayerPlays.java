package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.ActionResult.ErrorPlayResult;
import net.incongru.tichu.action.ActionResult.SuccessPlayResult;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;

class PlayerPlays implements Action<PlayerPlaysParam> {

    PlayerPlays() {
    }

    @Override
    public ActionResult exec(GameContext ctx, PlayerPlaysParam param) {
        final Player player = ctx.player(param.playerName());
        final Play.PlayResult res = ctx.game().currentRound().currentTrick().play(player, param.cards());

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
