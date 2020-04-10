package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

@Value.Immutable
public interface JoinTableParam extends ActionParam {

    int team();

    static WithActor<JoinTableParam> withActor(UserId player, int team) {
        return ImmutableWithActor.<JoinTableParam>builder()
                .actor(player)
                .param(ImmutableJoinTableParam.builder().team(team).build())
                .build();
    }
}
