package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
public interface PlayerPlaysParam extends ActionParam {

    Set<Card> cards();

    static WithActor<PlayerPlaysParam> withActor(UserId player, Set<Card> cards) {
        return new WithActor<>(player, ImmutablePlayerPlaysParam.builder().cards(cards).build());
    }
}
