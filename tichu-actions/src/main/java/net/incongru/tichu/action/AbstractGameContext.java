package net.incongru.tichu.action;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.UserId;

public abstract class AbstractGameContext implements GameContext {

    private final Object lock = new Object();
    private Game game;

    @Override
    public void newGame(Game game) {
        synchronized (this.lock) {
            if (this.game != null) {
                throw new IllegalStateException(
                    "GameContext has already been initialised"
                );
            }
            this.game = game;
        }
    }

    @Override
    public Game game() {
        return game;
    }

    @Override
    public Player player(UserId playerId) {
        return game().players().getPlayerById(playerId);
    }
}
