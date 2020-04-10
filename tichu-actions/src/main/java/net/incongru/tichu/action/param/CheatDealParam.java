package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
public interface CheatDealParam extends ActionParam {

    Set<Card> cards();

    static WithActor<CheatDealParam> withActor(UserId player, Set<Card> cards) {
        return ImmutableWithActor.<CheatDealParam>builder()
                .actor(player)
                .param(ImmutableCheatDealParam.builder().cards(cards).build())
                .build();
    }
}
