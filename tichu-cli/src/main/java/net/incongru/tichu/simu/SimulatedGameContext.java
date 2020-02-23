package net.incongru.tichu.simu;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;

public class SimulatedGameContext {
    private final Object lock = new Object();
    private Game game;

    public void newGame(Game game) {
        synchronized (this.lock) {
            if (this.game != null) {
                throw new IllegalStateException("GameContext has already been initialised");
            }
            this.game = game;
        }
    }

    public Game game() {
        return game;
    }

    public Player player(String playerName) {
        return game().players().getPlayerByName(playerName).orElseThrow(() -> new IllegalArgumentException("No player called " + playerName));
    }

    /**
     * Logs a message to the simulation.
     *
     * @see String#format(String, Object...)
     */
    public void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
