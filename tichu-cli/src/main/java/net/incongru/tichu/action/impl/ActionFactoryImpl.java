package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.model.Card;

import java.util.Collections;
import java.util.Set;

// TODO this factory is for simulated/fake games. We probably need a different impl for real games.

public class ActionFactoryImpl implements ActionFactory {
    // GameContext should allow actions to e.g check for playerName validity
    // Maybe Actions will have a validate() method before they get executed.

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
    public Action cheatDeal(String playerName, Set<Card> cards) {
        // TODO check we're in cheat/simu mode
        return new CheatDeal(playerName, cards);
    }

    @Override
    public Action plays(String playerName, Set<Card> cards) {
        return new PlayerPlays(playerName, cards);
    }

    @Override
    public Action passes(String playerName) {
        return new PlayerPlays(playerName, Collections.emptySet());
    }
}
