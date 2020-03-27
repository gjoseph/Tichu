package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
public interface JoinTableParam extends ActionParam {
    String playerName();

    int team();

    // Convenience ...
    static JoinTableParam with(String playerName, int team) {
        return ImmutableJoinTableParam.builder().playerName(playerName).team(team).build();
    }
}
