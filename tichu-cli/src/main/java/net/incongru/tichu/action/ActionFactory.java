package net.incongru.tichu.action;

import net.incongru.tichu.model.Card;

import java.util.Set;

/**
 * Creates Action instances.
 * Currently mostly exists so we can mock out this interface in tests.
 */
public interface ActionFactory {

    Action init();

    Action joinTeam(String playerName, int team);

    Action isReady(String playerName);

    Action cheatDeal(String playerName, Set<Card> cards);

    Action plays(String playerName, Set<Card> cards);

    Action passes(String playerName);
}
