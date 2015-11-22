package net.incongru.tichu.model;

import java.util.ArrayDeque;
import java.util.Queue;

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

    public Play.PlayResult play(Game.Player player, Play play) {
        // Is this even a valid play ?
        if (!rules.isValid(play)) {
            return new Play.PlayResult(Play.PlayResult.Result.INVALIDPLAY, "learn the rules");
        }

        // TODO this needs to be moved to TichuRules ?
        // Validate this player can play now - only nextPlayer can play, unless its a bomb
        if (!rules.isBomb(play) && !player.equals(nextPlayer)) {
            return new Play.PlayResult(Play.PlayResult.Result.INVALIDSTATE, "not your turn");
        }

        // Validate cards against last play
        final Play prevPlay = plays.peek();
        if (!rules.canPlayAfter(prevPlay, play)) {
            return new Play.PlayResult(Play.PlayResult.Result.TOOWEAK, "can't play this after " + prevPlay.toString());
        }

        return new Play.PlayResult(Play.PlayResult.Result.NEXTGOES, "next player pls");
    }

}
