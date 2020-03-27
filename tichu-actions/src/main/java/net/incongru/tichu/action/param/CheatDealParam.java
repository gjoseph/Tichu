package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
public interface CheatDealParam extends ActionParam {
    String playerName();

    Set<Card> cards();

    // Convenience ...
    static CheatDealParam with(String playerName, Set<Card> cards) {
        return ImmutableCheatDealParam.builder().playerName(playerName).cards(cards).build();
    }
}
