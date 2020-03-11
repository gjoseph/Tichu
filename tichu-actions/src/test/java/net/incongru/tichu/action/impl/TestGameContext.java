package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.impl.CheatDeal.CheatDealParam;
import net.incongru.tichu.action.impl.InitialiseGame.InitialiseGameParam;
import net.incongru.tichu.action.impl.JoinTable.JoinTableParam;
import net.incongru.tichu.action.impl.PlayerIsReady.PlayerIsReadyParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;

import java.util.Set;

class TestGameContext implements GameContext {
    TestGameContext initialised() {
        new InitialiseGame().exec(this, new InitialiseGameParam());
        return this;
    }

    TestGameContext withSamplePlayers() {
        // play order would be alex jules charlie quinn
        new JoinTable().exec(this, new JoinTableParam("alex", 0)); // player 1 (team 1)
        new JoinTable().exec(this, new JoinTableParam("charlie", 0));  // player 3 (team 1)
        new JoinTable().exec(this, new JoinTableParam("jules", 1));  // player 2 (team 2)
        new JoinTable().exec(this, new JoinTableParam("quinn", 1)); // player 4 (team 2)
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext withCards(Set<Card> forAlex, Set<Card> forCharlie, Set<Card> forJules, Set<Card> forQuinn) {
        new CheatDeal().exec(this, new CheatDealParam("alex", forAlex));
        new CheatDeal().exec(this, new CheatDealParam("charlie", forCharlie));
        new CheatDeal().exec(this, new CheatDealParam("jules", forJules));
        new CheatDeal().exec(this, new CheatDealParam("quinn", forQuinn));
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext allReady() {
        new PlayerIsReady().exec(this, new PlayerIsReadyParam("alex"));
        new PlayerIsReady().exec(this, new PlayerIsReadyParam("charlie"));
        new PlayerIsReady().exec(this, new PlayerIsReadyParam("jules"));
        new PlayerIsReady().exec(this, new PlayerIsReadyParam("quinn"));
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
