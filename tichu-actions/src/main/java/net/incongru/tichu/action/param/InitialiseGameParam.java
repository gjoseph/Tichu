package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

@Value.Immutable
public interface InitialiseGameParam extends ActionParam {

    static WithActor<InitialiseGameParam> withActor(UserId player) {
        return ImmutableWithActor.<InitialiseGameParam>builder()
                .actor(player)
                .param(ImmutableInitialiseGameParam.builder().build())
                .build();
    }
}
