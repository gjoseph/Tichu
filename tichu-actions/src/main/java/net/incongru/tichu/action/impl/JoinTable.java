package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Team;

class JoinTable implements Action {
    private final String playerName;
    private final int team;

    JoinTable(String playerName, int team) {
        this.playerName = playerName;
        this.team = team;
    }

    @Override
    public Result exec(GameContext ctx) {
        // TODO validating team number should be role of action/rules, but where does error bubble up if invalid ?
        final Players players = ctx.game().players();
        final Team team = players.getTeam(this.team);
        players.join(new Player(playerName), team);
        return new Success() {
//                return playerName + " joined team " + team;
        };
    }
}
