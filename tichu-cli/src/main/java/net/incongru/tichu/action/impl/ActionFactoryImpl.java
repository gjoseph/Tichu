package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;

public class ActionFactoryImpl implements ActionFactory {
    // TODO the actions will probably be injected with _stuff_, e.g. some sort of GameContext

    @Override
    public Action init() {
        return new InitialiseGame();
    }

    @Override
    public Action joinTeam(String playerName, int team) {
        return new JoinTable(playerName, team);
    }

}
