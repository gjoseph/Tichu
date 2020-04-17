package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.model.Player;

class CheatDeal implements Action<CheatDealParam> {

    CheatDeal() {
        // TODO check we're in cheat/simu mode
    }

    @Override
    public ActionResponse exec(GameContext ctx, ActionParam.WithActor<CheatDealParam> param) {
        if (ctx.game().isStarted()) {
            throw new IllegalStateException("Game is already started");
        }
        if (ctx.game().isReadyToStart()) {
            throw new IllegalStateException("Game is ready to start, too late to cheat");
        }
        final Player player = ctx.player(param.actor());
        param.param().cards().forEach(c -> player.deal(c));

        return new SimpleResponse(param.actor(), ActionType.cheatDeal, CheatDealResult.OK);
    }

}
