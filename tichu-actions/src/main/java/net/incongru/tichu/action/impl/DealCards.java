package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.DealCardsParam;

class DealCards implements Action<DealCardsParam, DealCardsResult> {

    DealCards() {}

    @Override
    public ActionResponse<DealCardsResult> exec(
        GameContext ctx,
        ActionParam.WithActor<DealCardsParam> param
    ) {
        // TODO net.incongru.tichu.action.impl.PlayerIsReady currently deals by means of calling
        // net.incongru.tichu.model.Round.start
        throw new IllegalStateException("not implemented yet");
    }
}
