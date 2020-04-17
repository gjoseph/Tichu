package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.ActionResponse;

public enum JoinTableResult implements ActionResponse.Result {
    CAN_NOT_JOIN_FULL_TABLE(false),
    OK, OK_TABLE_IS_NOW_FULL;

    private final boolean success;

    JoinTableResult() {
        this(true);
    }

    JoinTableResult(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccessful() {
        return this.success;
    }
}
