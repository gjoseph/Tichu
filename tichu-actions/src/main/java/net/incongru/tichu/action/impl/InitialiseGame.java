package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Team;
import net.incongru.tichu.model.TichuRules;

class InitialiseGame implements Action<InitialiseGameParam, InitialiseGameResult> {
    @Override
    public ActionResponse<InitialiseGameResult> exec(GameContext ctx, ActionParam.WithActor<InitialiseGameParam> param) {
        final Players players = new Players();
        players.add(new Team("Team 1"));
        players.add(new Team("Team 2"));

        final Game game = newGame(players);
        ctx.newGame(game);

        // TODO other possible responses:
        // game already started, perhaps team setup, rules, etc
        return new SimpleResponse<>(
                param.actor(),
                ActionType.init,
                InitialiseGameResult.OK
        );
    }

    protected Game newGame(Players players) {
        final TichuRules rules = new TichuRules();
        return new Game(players, rules);
    }
}
