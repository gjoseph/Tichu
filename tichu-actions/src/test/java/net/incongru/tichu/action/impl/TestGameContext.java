package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;

import java.util.Set;

class TestGameContext implements GameContext {
    TestGameContext initialised() {
        new InitialiseGame().exec(this);
        return this;
    }

    TestGameContext withSamplePlayers() {
        // play order would be alex jules charlie quinn
        new JoinTable("alex", 0).exec(this); // player 1 (team 1)
        new JoinTable("charlie", 0).exec(this);  // player 3 (team 1)
        new JoinTable("jules", 1).exec(this);  // player 2 (team 2)
        new JoinTable("quinn", 1).exec(this); // player 4 (team 2)
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext withCards(Set<Card> forAlex, Set<Card> forCharlie, Set<Card> forJules, Set<Card> forQuinn) {
        new CheatDeal("alex", forAlex).exec(this);
        new CheatDeal("charlie", forCharlie).exec(this);
        new CheatDeal("jules", forJules).exec(this);
        new CheatDeal("quinn", forQuinn).exec(this);
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext allReady() {
        new PlayerIsReady("alex").exec(this);
        new PlayerIsReady("charlie").exec(this);
        new PlayerIsReady("jules").exec(this);
        new PlayerIsReady("quinn").exec(this);
        return this;
    }

    // This is a copy of SimulatedGameContext ...
    private final Object lock = new Object();
    private Game game;

    TestGameContext() {
    }

    @Override
    public void newGame(Game game) {
        synchronized (this.lock) {
            if (this.game != null) {
                throw new IllegalStateException("GameContext has already been initialised");
            }
            this.game = game;
        }
    }

    @Override
    public Game game() {
        return game;
    }

    @Override
    public Player player(String playerName) {
        return game().players().getPlayerByName(playerName).orElseThrow(() -> new IllegalArgumentException("No player called " + playerName));
    }

    /**
     * Logs a message to the simulation.
     *
     * @see String#format(String, Object...)
     */
    @Override
    public void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
