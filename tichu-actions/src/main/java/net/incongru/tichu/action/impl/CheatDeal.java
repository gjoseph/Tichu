package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.ActionResult.Success;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Player;

import java.util.Set;

class CheatDeal implements Action<CheatDeal.CheatDealParam> {

    CheatDeal() {
        // TODO check we're in cheat/simu mode
    }

    @Override
    public Class<CheatDealParam> paramType() {
        return CheatDealParam.class;
    }

    @Override
    public ActionResult exec(GameContext ctx, CheatDealParam param) {
        if (ctx.game().isStarted()) {
            throw new IllegalStateException("Game is already started");
        }
        if (ctx.game().isReadyToStart()) {
            throw new IllegalStateException("Game is ready to start, too late to cheat");
        }
        final Player player = ctx.player(param.playerName);
        param.cards.forEach(c -> player.deal(c));

        return new Success() {
        };
    }

    static class CheatDealParam implements ActionParam {
        private final String playerName;
        private final Set<Card> cards;
        // TODO Immutables and jackson mapping

        public CheatDealParam(String playerName, Set<Card> cards) {
            this.playerName = playerName;
            this.cards = cards;
        }
    }
}
