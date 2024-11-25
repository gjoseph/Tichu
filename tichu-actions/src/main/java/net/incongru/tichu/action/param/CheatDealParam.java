package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;

import java.util.Set;

public record CheatDealParam(
        Set<Card> cards
) implements ActionParam {

    public static WithActor<CheatDealParam> withActor(UserId player, Set<Card> cards) {
        return new WithActor<>(player, new CheatDealParam(cards));
    }
}
