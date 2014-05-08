package net.incongru.tichu.model;

import java.util.Set;

import lombok.Value;

/**
 * A trick is a series of {@link Play}s leading to one player collecting the cards played during the trick.
 */
public class Trick {
    // starting player
    // last or next player
    // play(player, Set<Card>)
    //    -- validate player
    //    -- validate cards
    // return .. ? enum ?
    private Game.Player nextPlayer;

    public Trick(Game.Player firstPlayer) {
        this.nextPlayer = firstPlayer;
    }


    /**
     * One card or set of cards that a player puts down. An empty set means the player passes.
     */
    @Value
    static public class Play {
        Set<Card> cards;
    }


    @Value
    static public class PlayResult {
        enum Result {CONTINUE, TAKE, INVALID}

        Result result;
        String message;
    }
}
