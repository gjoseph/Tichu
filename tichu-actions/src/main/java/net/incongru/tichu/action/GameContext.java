package net.incongru.tichu.action;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;

public interface GameContext {
    void newGame(Game game);

    Game game();

    Player player(String playerName);

    void log(String msg, Object... args);
}
