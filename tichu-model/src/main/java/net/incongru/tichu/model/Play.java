package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Set;
import net.incongru.tichu.model.card.Card;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * One card or set of cards that a player puts down. An empty set means the player passes.
 */
public interface Play<P extends Play> {
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

    interface PlayFactory<P extends Play> {
        /**
         * Returns a play of this type if it is contained in the given cards, null otherwise.
         * In other words, this method will return a candidate found in the given cards, and
         * ignore any additional cards.
         */
        default P findIn(Set<Card> hand) {
            throw new IllegalStateException("not implemented yet"); // TODO
        }

        /**
         * Like {@link #findIn(Set)}, but returns all the possible candidates of this type.
         */
        default List<P> findAllIn(Set<Card> hand) {
            throw new IllegalStateException("not implemented yet"); // TODO
        }

        /**
         * Returns a Play if the given cards are of that type, null otherwise.
         */
        @Nullable
        P is(Set<Card> cards);
    }

    class PlayResult {

        public enum Result {
            NEXTGOES,
            TRICK_END,
            TOOWEAK,
            NOTINHAND,
            INVALIDPLAY,
            INVALIDSTATE,
        }

        private final @Nullable Play play;
        private final Result result;
        private final String message; // TODO ditch message from here. Result is enough info for client/ui to generate a message.

        // TODO different. classes to avoid @NullablePlay
        public PlayResult(Result result, String message) {
            Preconditions.checkArgument(
                result == Result.NOTINHAND || result == Result.INVALIDSTATE,
                "Must specify a Play with result %s",
                result
            );
            this.play = null;
            this.result = result;
            this.message = message;
        }

        public PlayResult(Play play, Result result, String message) {
            Preconditions.checkNotNull(
                play,
                "Must specify a Play with result %s",
                result
            );
            this.play = play;
            this.result = result;
            this.message = message;
        }

        public @Nullable Play play() {
            return play;
        }

        public Result result() {
            return result;
        }

        public String message() {
            return message;
        }
    }
}
