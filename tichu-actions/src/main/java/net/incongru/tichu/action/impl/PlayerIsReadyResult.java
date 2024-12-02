package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.ActionResponse;

public enum PlayerIsReadyResult implements ActionResponse.Result {
    OK,
    OK_STARTED;

    private final boolean success;

    PlayerIsReadyResult() {
        this(true);
    }

    PlayerIsReadyResult(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccessful() {
        return this.success;
    }
}
