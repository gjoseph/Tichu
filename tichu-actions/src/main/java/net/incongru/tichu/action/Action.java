package net.incongru.tichu.action;


public interface Action {
    ActionResult exec(GameContext ctx);

    enum ActionType {
        init, join, isReady, cheatDeal,
        play, pass
    }

}
