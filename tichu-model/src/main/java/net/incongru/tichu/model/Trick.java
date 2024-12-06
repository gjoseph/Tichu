package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.plays.Initial;
import net.incongru.tichu.model.plays.Pass;
import net.incongru.tichu.model.util.DeckConstants;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A trick is a series of {@link Play}s leading to one player collecting the cards played during the trick.
 * // TODO log all plays - at game, round or trick level
 */
public class Trick {

    private final TichuRules rules;
    private final Deque<Played> plays;
    private final Iterator<Player> playersCycle;
    private @Nullable Player currentPlayer;

    public Trick(TichuRules rules, Iterator<Player> playersCycle) {
        this.rules = rules;
        this.playersCycle = playersCycle;
        this.currentPlayer = playersCycle.next(); // we could probably get rid of `currentPlayer` and just use playersCycle.peek() ?
        this.plays = new LinkedList<>();
        plays.add(new Played(null, Initial.INSTANCE));
    }

    // TODO this should pbly not be public and move up to Round or even Game - so we can control flow?
    public Play.PlayResult play(Player player, Set<Card> cards) {
        if (isDone()) {
            return new Play.PlayResult(
                Play.PlayResult.Result.INVALIDSTATE,
                "trick is over"
            );
        }

        // Does the player have these cards?
        if (!player.hand().hasAll(cards)) {
            // TODO cheat ?
            return new Play.PlayResult(
                Play.PlayResult.Result.NOTINHAND,
                "You don't have those cards!"
            );
        }

        // Is this a correct combination ?
        final Play play = rules.validate(cards);

        // Is this a valid play ?
        if (!rules.isValid(play)) {
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.INVALIDPLAY,
                "This is not a valid combination"
            );
        }

        // TODO this needs to be moved to TichuRules ?
        // Validate this player can play now - only currentPlayer can play, unless its a bomb
        if (!rules.isBomb(play) && !player.equals(currentPlayer)) {
            // TODO return different PlayResult.Result
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.INVALIDSTATE,
                "not your turn"
            );
        }

        // Validate cards against last play
        final Play prevPlay = previousNonPass().play();
        if (!rules.canPlayAfter(prevPlay, play)) {
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.TOOWEAK,
                "can't play this after " + prevPlay.toString()
            );
        }

        plays.add(new Played(currentPlayer, play));
        player.discard(cards);
        if (isDone()) {
            currentPlayer = null;
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.TRICK_END,
                "congrats"
            );
        } else if (isDog(play)) {
            currentPlayer = teamMateOrNextPlayer();
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.TRICK_END,
                "woof"
            );
        } else {
            currentPlayer = playersCycle.next();
            return new Play.PlayResult(
                play,
                Play.PlayResult.Result.NEXTGOES,
                "next player pls"
            );
        }
    }

    private Player teamMateOrNextPlayer() {
        // Of course this will be buggy
        playersCycle.next();
        return playersCycle.next();
    }

    public Player currentPlayer() {
        Preconditions.checkState(
            !isDone(),
            "Trick is done, there is no current player"
        );
        Preconditions.checkState(
            currentPlayer != null,
            "Trick is not done, but there is no current player, this should never happen"
        );
        // we've checked it's not-null, not sure why checker isn't happy about this
        return currentPlayer;
    }

    // TODO not public
    public Played previousNonPass() {
        final Iterator<Played> it = plays.descendingIterator();
        while (it.hasNext()) {
            final Played last = it.next();
            if (NO_PASS.test(last.play())) {
                return last;
            }
        }
        // At a minimum, we'll have the Initial play
        throw new IllegalStateException(
            "There has to be at least one non-pass play in the trick"
        );
    }

    // TODO move this to Rules?
    public boolean isDone() {
        // Has anything been played at all ?
        final boolean hasAnyNonPassPlay = plays
            .stream()
            .map(Played::play)
            .anyMatch(NO_PASS_NOR_INITIAL);
        // Could probably be optimized. For now, we'll just check the last three plays were passes, so we can assume nothing can happen anymore
        return (
            hasAnyNonPassPlay &&
            Functions.lastNMatches(
                plays,
                3,
                Predicates.compose(Pass.class::isInstance, Played::play)
            )
        );
    }

    private boolean isDog(Play play) {
        return play.getCards().equals(Set.of(DeckConstants.Dog));
    }

    /**
     * A predicate that checks a play is neither a Pass or an "Initial".
     */
    private static final Predicate<Play> NO_PASS_NOR_INITIAL =
        ((Predicate<Play>) Pass.class::isInstance).or(
                Initial.class::isInstance
            ).negate();
    private static final Predicate<Play> NO_PASS =
        ((Predicate<Play>) Pass.class::isInstance).negate();
}
