package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
public interface CheatDealParam extends ActionParam {

    Set<Card> cards();

    static WithActor<CheatDealParam> withActor(UserId player, Set<Card> cards) {
        return new WithActor<>(player,
                ImmutableCheatDealParam.builder().cards(cards).build());
    }
}
