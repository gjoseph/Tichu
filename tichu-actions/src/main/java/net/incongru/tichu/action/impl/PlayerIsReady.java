package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.model.Game;

class PlayerIsReady implements Action<PlayerIsReadyParam> {

    PlayerIsReady() {
    }

    @Override
    public ActionResponse exec(GameContext ctx, ActionParam.WithActor<PlayerIsReadyParam> param) {
        ctx.player(param.actor()).setReady();
        final Game game = ctx.game();
        if (game.players().areAllReady()) {
            game.start(); // TODO do we want to check isReadyToStart?
            game.currentRound().start(); // TODO see net.incongru.tichu.model.Round.start
            return new SimpleActionResponse(param.actor(), ActionType.isReady, PlayerIsReadyResult.OK_STARTED);
        } else {
            return new SimpleActionResponse(param.actor(), ActionType.isReady, PlayerIsReadyResult.OK);
        }
    }


    enum PlayerIsReadyResult implements ActionResponse.Result {
        OK, OK_STARTED;

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
}
