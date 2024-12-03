package net.incongru.tichu.action.impl;

import com.google.common.annotations.VisibleForTesting;
import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.model.UserId;

/**
 * {@link ActionResponse} for actions that don't require additional params in their response.
 */
public class SimpleResponse<R extends ActionResponse.Result>
    implements ActionResponse<R> {

    private final UserId actor;
    private final Action.ActionType actionType;
    private final R result;
    private final Message message;

    @VisibleForTesting
    public SimpleResponse(
        UserId actor,
        Action.ActionType actionType,
        R result
    ) {
        this(
            actor,
            actionType,
            result,
            new Message(actionType + " was " + result)
        );
    }

    SimpleResponse(
        UserId actor,
        Action.ActionType actionType,
        R result,
        Message message
    ) {
        this.actor = actor;
        this.actionType = actionType;
        this.result = result;
        this.message = message;
    }

    @Override
    public UserId actor() {
        return actor;
    }

    @Override
    public Action.ActionType forAction() {
        return actionType;
    }

    @Override
    public R result() {
        return result;
    }

    @Override
    public Message message() {
        return message;
    }

    @Override
    public String toString() {
        return (
            "SimpleResponse{" +
            "actor=" +
            actor +
            ", actionType=" +
            actionType +
            ", result=" +
            result +
            ", message=" +
            message +
            '}'
        );
    }
}
