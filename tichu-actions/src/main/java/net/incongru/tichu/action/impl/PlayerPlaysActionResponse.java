package net.incongru.tichu.action.impl;

import com.fasterxml.jackson.annotation.JsonGetter;
import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.UserId;

public class PlayerPlaysActionResponse extends SimpleActionResponse<PlayerPlaysActionResponse.PlayerPlaysResult> {
    private final Play play;
    private final UserId nextPlayer;

    PlayerPlaysActionResponse(UserId actor, Play.PlayResult result, UserId nextPlayer, Message msg) {
        super(actor, Action.ActionType.play, toResult(result.result()), msg);
        this.play = result.play();
        this.nextPlayer = nextPlayer;
    }

    // TODO or expose a PlayView instead? Is serialising Play as-is safe?
    @JsonGetter
    public Play play() {
        return this.play;
    }

    @JsonGetter
    UserId nextPlayer() {
        return this.nextPlayer;
    }

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

    private static PlayerPlaysResult toResult(Play.PlayResult.Result res) {
        // from Play.PlayResult to PlayerPlaysResult
        switch (res) {
            case TRICK_END:
                return PlayerPlaysResult.TRICK_END;
            case NEXTGOES:
                return PlayerPlaysResult.NEXTGOES;
            case TOOWEAK:
                return PlayerPlaysResult.TOOWEAK;
            case INVALIDPLAY:
                return PlayerPlaysResult.INVALIDPLAY;
            case INVALIDSTATE:
                return PlayerPlaysResult.INVALIDSTATE;
            case NOTINHAND:
                return PlayerPlaysResult.NOTINHAND;
            default:
                throw new IllegalStateException("Unknown result type :" + res);
        }
    }
}
