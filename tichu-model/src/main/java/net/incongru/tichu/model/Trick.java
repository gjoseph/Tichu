package net.incongru.tichu.model;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

import lombok.Value;

/**
 * A trick is a series of {@link Play}s leading to one player collecting the cards played during the trick.
 * // TODO log all plays - at game, round or trick level
 */
public class Trick {
    // play(player, Set<Card>)
    //    -- validate player
    //    -- validate cards
    // return .. ? enum ?
    private final TichuRules rules;
    private final Queue<Play> plays;
    private Game.Player nextPlayer;

    public Trick(TichuRules rules, Game.Player firstPlayer) {
        this.rules = rules;
        this.nextPlayer = firstPlayer;
        this.plays = new ArrayDeque<>();
    }

    public PlayResult play(Game.Player player, Play play) {
        // Validate this player can play now
        if (!rules.isBomb(play) && !player.equals(nextPlayer)) {
            return new PlayResult(PlayResult.Result.INVALIDSTATE, "not your turn");
        }

        // Validate cards against last play
        final Play prevPlay = plays.peek();

        return null;
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
        enum Result {NEXTGOES, TAKE, INVALIDPLAY, INVALIDSTATE}

        Result result;
        String message;
    }
}
