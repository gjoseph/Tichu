package net.incongru.tichu.action.param;

import java.util.Set;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.card.Card;

public record CheatDealParam(Set<Card> cards) implements ActionParam {
    public static WithActor<CheatDealParam> withActor(
        UserId player,
        Set<Card> cards
    ) {
        return new WithActor<>(player, new CheatDealParam(cards));
    }
}
