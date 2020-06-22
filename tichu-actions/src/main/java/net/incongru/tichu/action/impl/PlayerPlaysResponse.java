package net.incongru.tichu.action.impl;

import com.google.common.annotations.VisibleForTesting;
import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.UserId;

public class PlayerPlaysResponse extends SimpleResponse<PlayerPlaysResult> {
    private final Play play;
    private final UserId nextPlayer;

    @VisibleForTesting
    public PlayerPlaysResponse(UserId actor, Play.PlayResult result, UserId nextPlayer, Message msg) {
        super(actor, Action.ActionType.PLAY, toResult(result.result()), msg);
        this.play = result.play();
        this.nextPlayer = nextPlayer;
    }

    // TODO or expose a PlayView instead? Is serialising Play as-is safe?
    // @JsonView might be an option?
    public Play play() {
        return this.play;
    }

    UserId nextPlayer() {
        return this.nextPlayer;
    }

    private static PlayerPlaysResult toResult(Play.PlayResult.Result res) {
        // from Play.PlayResult to PlayerPlaysResult
        switch (res) {
            case TRICK_END:
                return PlayerPlaysResult.TRICK_END;
            case NEXTGOES:
                return PlayerPlaysResult.NEXT_PLAYER_GOES;
            case TOOWEAK:
                return PlayerPlaysResult.TOO_WEAK;
            case INVALIDPLAY:
                return PlayerPlaysResult.INVALID_PLAY;
            case INVALIDSTATE:
                return PlayerPlaysResult.INVALID_STATE;
            case NOTINHAND:
                return PlayerPlaysResult.NOT_IN_HAND;
            default:
                throw new IllegalStateException("Unknown result type :" + res);
        }
    }
}
