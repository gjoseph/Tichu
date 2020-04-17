package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.ActionResponse;

public enum PlayerPlaysResult implements ActionResponse.Result {
    NEXTGOES,
    TRICK_END,
    TOOWEAK(false),
    NOTINHAND(false),
    INVALIDPLAY(false),
    INVALIDSTATE(false);

    private final boolean success;

    PlayerPlaysResult() {
        this(true);
    }

    PlayerPlaysResult(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccessful() {
        return this.success;
    }
}
