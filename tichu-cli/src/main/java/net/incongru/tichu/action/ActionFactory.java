package net.incongru.tichu.action;

/**
 * Creates Action instances.
 * Currently mostly exists so we can mock out this interface in tests.
 */
public interface ActionFactory {

    Action init();

    Action joinTeam(String playerName, int team);

    Action isReady(String playerName);
}
