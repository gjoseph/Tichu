package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;

public record InitialiseGameParam() implements ActionParam {

    public static WithActor<InitialiseGameParam> withActor(UserId player) {
        return new WithActor<>(player, new InitialiseGameParam());
    }
}
