package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.UserId;

public record JoinTableParam(int team) implements ActionParam {

    public static WithActor<JoinTableParam> withActor(UserId player, int team) {
        return new WithActor<>(player, new JoinTableParam(team));
    }
}
