package net.incongru.tichu.action.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutablePlayerPlaysParam.class)
@JsonDeserialize(as = ImmutablePlayerPlaysParam.class)
public interface PlayerPlaysParam extends ActionParam {
    String playerName();

    Set<Card> cards();
    // TODO jackson mapping?

    // Convenience ...
    static PlayerPlaysParam with(String playerName, Set<Card> cards) {
        return ImmutablePlayerPlaysParam.builder().playerName(playerName).cards(cards).build();
    }
}
