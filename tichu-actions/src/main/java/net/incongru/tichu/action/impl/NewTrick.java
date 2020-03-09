package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Trick;

class NewTrick implements Action {
    @Override
    public Result exec(GameContext ctx) {
        final Trick trick = ctx.game().currentRound().newTrick();
        ctx.log("New trick! %s", trick);
        return new Success() {
            @Override
            public String toString() {
                return "New trick started: " + trick;
            }
        };
    }
}
