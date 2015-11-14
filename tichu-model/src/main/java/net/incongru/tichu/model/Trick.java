package net.incongru.tichu.model;

import java.util.ArrayDeque;
import java.util.List;
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
        // Is this even a valid play ?
        if (!rules.isValid(play)) {
            return new PlayResult(PlayResult.Result.INVALIDPLAY, "learn the rules");
        }

        // TODO this needs to be moved to TichuRules ?
        // Validate this player can play now - only nextPlayer can play, unless its a bomb
        if (!rules.isBomb(play) && !player.equals(nextPlayer)) {
            return new PlayResult(PlayResult.Result.INVALIDSTATE, "not your turn");
        }

        // Validate cards against last play
        final Play prevPlay = plays.peek();
        if (!rules.canPlayAfter(prevPlay, play)) {
            return new PlayResult(PlayResult.Result.TOOWEAK, "can't play this after " + prevPlay.toString());
        }

        return new PlayResult(PlayResult.Result.NEXTGOES, "next player pls");
    }

    /**
     * One card or set of cards that a player puts down. An empty set means the player passes.
     */
    public static interface Play<P extends Play> {
        Set<Card> getCards();

        /**
         * Validates this play can be played after the previous one.
         * This method is not called when opening a trick, since all plays can open a trick,
         * while some plays (MahJong, Dog) can *only* be used to open a trick, so they'll return false.
         */
        boolean canBePlayedAfter(Play other);

        boolean isBomb();

        /**
         * Returns the name of this (kind of) play. (e.g "Pair")
         */
        String name();

        /**
         * Describes this play by the cards it uses. (e.g "Pair of eights")
         */
        String describe();


        static interface PlayFactory<P extends Play> {
            /**
             * Returns a play if this type of play is contained in the given cards, null otherwise.
             */
            P findIn(Set<Card> hand);

            List<P> findAllIn(Set<Card> hand);

            /**
             * Returns a Play if the given cards are of that type, null otherwise.
             */
            P is(Set<Card> cards);
        }
    }

    @Value
    static public class PlayResult {
        enum Result {NEXTGOES, TAKE, TOOWEAK, INVALIDPLAY, INVALIDSTATE}

        Result result;
        String message;
    }
}
