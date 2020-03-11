package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.ActionResult.Success;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Game;

class PlayerIsReady implements Action<PlayerIsReady.PlayerIsReadyParam> {

    PlayerIsReady() {
    }

    @Override
    public Class<PlayerIsReadyParam> paramType() {
        return PlayerIsReadyParam.class;
    }

    @Override
    public ActionResult exec(GameContext ctx, PlayerIsReadyParam param) {
        ctx.player(param.playerName).setReady();
        final Game game = ctx.game();
        if (game.players().areAllReady()) {
            game.start(); // TODO do we want to check isReadyToStart?
            game.currentRound().start(); // TODO see net.incongru.tichu.model.Round.start
            return new Success() {
                // game started
            };
        } else {
            return new Success() {
                // played marked ready but game not started
            };
        }
    }


    static class PlayerIsReadyParam implements ActionParam {
        private final String playerName;
        // TODO Immutables and jackson mapping
        public PlayerIsReadyParam(String playerName) {
            this.playerName = playerName;
        }
    }
}