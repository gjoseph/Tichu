package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.TichuRules;

class CheatDeal implements Action<CheatDealParam, CheatDealResult> {

    CheatDeal() {}

    @Override
    public ActionResponse<CheatDealResult> exec(
        GameContext ctx,
        ActionParam.WithActor<CheatDealParam> param
    ) {
        // TODO This is sloppy...
        if (ctx.game().rules().getClass().equals(TichuRules.class)) {
            throw new IllegalStateException(
                "Can't cheat-deal with the regular rules class"
            );
        }

        if (ctx.game().isStarted()) {
            throw new IllegalStateException("Game is already started");
        }
        if (ctx.game().isReadyToStart()) {
            throw new IllegalStateException(
                "Game is ready to start, too late to cheat"
            );
        }
        final Player player = ctx.player(param.actor());
        param.param().cards().forEach(c -> player.deal(c));

        return new SimpleResponse<>(
            param.actor(),
            ActionType.CHEAT_DEAL,
            CheatDealResult.OK
        );
    }
}
