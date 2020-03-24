package net.incongru.tichu.action.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableJoinTableParam.class)
@JsonDeserialize(as = ImmutableJoinTableParam.class)
public interface JoinTableParam extends ActionParam {
    String playerName();

    int team();
    // TODO jackson mapping?

    // Convenience ...
    static JoinTableParam with(String playerName, int team) {
        return ImmutableJoinTableParam.builder().playerName(playerName).team(team).build();
    }
}
