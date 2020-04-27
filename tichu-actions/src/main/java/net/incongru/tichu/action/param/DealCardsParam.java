package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;

public record DealCardsParam(int howManyCards) implements ActionParam {
    public static WithActor<DealCardsParam> withActor(
        UserId player,
        int howManyCards
    ) {
        return new WithActor<>(player, new DealCardsParam(howManyCards));
    }
}
