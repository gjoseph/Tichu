package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;

public record NewTrickParam() implements ActionParam {

    public static WithActor<NewTrickParam> withActor(UserId player) {
        return new WithActor<>(player, new NewTrickParam());
    }
}
