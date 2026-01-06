package net.incongru.tichu.action;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.UserId;

public interface GameContext {
    void newGame(Game game);

    Game game();

    Player player(UserId playerId);

    void log(String msg, Object... args);

    void debug(String msg, Object... args);
}
