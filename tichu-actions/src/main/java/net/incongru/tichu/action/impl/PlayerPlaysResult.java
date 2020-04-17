package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.ActionResponse;

public enum PlayerPlaysResult implements ActionResponse.Result {
    NEXT_PLAYER_GOES,
    TRICK_END,
    TOO_WEAK(false),
    NOT_IN_HAND(false),
    INVALID_PLAY(false),
    INVALID_STATE(false);

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
