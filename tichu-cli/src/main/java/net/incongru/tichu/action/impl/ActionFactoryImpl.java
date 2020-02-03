package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.model.Card;

import java.util.List;

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

    @Override
    public Action isReady(String playerName) {
        return new PlayerIsReady(playerName);
    }

    @Override
    public Action cheatDeal(String playerName, List<Card> cards) {
        // TODO check we're in cheat/simu mode
        return new CheatDeal(playerName, cards);
    }
}
