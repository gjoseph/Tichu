package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;

import java.util.Set;

public record PlayerPlaysParam(
        Set<Card> cards
) implements ActionParam {

  public  static WithActor<PlayerPlaysParam> withActor(UserId player, Set<Card> cards) {
        return new ActionParam.WithActor<>(player, new PlayerPlaysParam(cards));
    }
}
