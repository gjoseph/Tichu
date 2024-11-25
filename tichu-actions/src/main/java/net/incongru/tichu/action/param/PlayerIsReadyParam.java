package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;

public record PlayerIsReadyParam() implements ActionParam {

    public static WithActor<PlayerIsReadyParam> withActor(UserId player) {
        return new WithActor<>(player, new PlayerIsReadyParam());
    }
}
