package net.incongru.tichu.simu;

import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;

public class SimulatedGameContext implements GameContext {
    private final Object lock = new Object();
    private Game game;

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
