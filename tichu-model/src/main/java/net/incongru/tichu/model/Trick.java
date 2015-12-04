package net.incongru.tichu.model;

import net.incongru.tichu.model.plays.Initial;
import net.incongru.tichu.model.plays.Pass;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;


/**
 * A trick is a series of {@link Play}s leading to one player collecting the cards played during the trick.
 * // TODO log all plays - at game, round or trick level
 */
public class Trick {
    private final TichuRules rules;
    private final Deque<Play> plays;
    private final Iterator<Players.Player> playersCycle;
    private Players.Player nextPlayer;

    // TODO take only one of cycle or whostarts (but cycle.peek shows next, not current)
    public Trick(TichuRules rules, Iterator<Players.Player> playersCycle, Players.Player whoStarts) {
        this.rules = rules;
        this.playersCycle = playersCycle;
        this.nextPlayer = whoStarts;
        this.plays = new LinkedList<>();
        plays.add(Initial.INSTANCE);
    }

    // TODO this should pbly not be public and move up to Round or even Game - so we can control flow?
    public Play.PlayResult play(Players.Player player, Set<Card> cards) {
        // Does the player have these cards?
        if (!player.hand().containsAll(cards)) {
            // TODO cheat ?
            return new Play.PlayResult(Optional.empty(), Play.PlayResult.Result.INVALIDPLAY, "You don't have those cards!");
        }

        // Is this a correct combination ?
        // TODO rules.validate should pbly be returning the Optional<Play>
        final Play play = rules.validate(cards);
        final Optional<Play> play1 = Optional.of(play);

        // Is this a valid play ?
        if (!rules.isValid(play)) {
            return new Play.PlayResult(play1, Play.PlayResult.Result.INVALIDPLAY, "This is not a valid combination");
        }

        // TODO this needs to be moved to TichuRules ?
        // Validate this player can play now - only nextPlayer can play, unless its a bomb
        if (!rules.isBomb(play) && !player.equals(nextPlayer)) {
            return new Play.PlayResult(play1, Play.PlayResult.Result.INVALIDSTATE, "not your turn");
        }

        // Validate cards against last play
        final Play prevPlay = plays.peekLast();
        if (!rules.canPlayAfter(prevPlay, play)) {
            return new Play.PlayResult(play1, Play.PlayResult.Result.TOOWEAK, "can't play this after " + prevPlay.toString());
        }

        plays.add(play);
        player.discard(cards);
        nextPlayer = playersCycle.next();
        return new Play.PlayResult(play1, Play.PlayResult.Result.NEXTGOES, "next player pls");
    }

    // TODO dubious method name, might imply we're _going_ to next player
    public Players.Player nextPlayer() {
        return nextPlayer;
    }

    // TODO move this to Rules?
    public boolean isDone() {
        // Could probably be optimized. For now, we'll just check the last three plays were passes, so we can assume nothing can happen anymore
        final boolean hasAnyNonPassPlay = plays.stream().anyMatch(NOPASS);
        return hasAnyNonPassPlay && Functions.lastNMatches(plays, 3, Pass.class::isInstance);
    }

    /**
     * A predicate that checks a play is neither a Pass or an "Initial".
     */
    private static final Predicate<Play> NOPASS = ((Predicate<Play>) Pass.class::isInstance).or(Initial.class::isInstance).negate();

}
