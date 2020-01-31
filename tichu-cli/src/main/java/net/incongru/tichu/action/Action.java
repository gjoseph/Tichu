package net.incongru.tichu.action;

public interface Action {
    // type ?
    // description? (would be useful for a log in the client/ui for example)
    Result exec();

    enum ActionType {
        init, join, isReady, cheatDeal,
        play, pass
    }

    interface Result {
    }

}
