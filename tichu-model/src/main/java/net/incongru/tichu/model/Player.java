package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import net.incongru.tichu.model.card.Card;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 *
 */
public class Player {

    private final UserId id;
    private boolean ready = false;
    private final Hand hand = new Hand();
    private final Set<Card> wonCards = new LinkedHashSet<>();

    public Player(@Nonnull UserId id) {
        Preconditions.checkNotNull(id, "Player name can't be null");
        this.id = id;
        this.ready = false;
    }

    public UserId id() {
        return id;
    }

    public void setReady() {
        if (this.ready) {
            throw new IllegalStateException(id() + " is already ready");
        }
        this.ready = true;
    }

    public boolean isReady() {
        return this.ready;
    }

    public void reclaimCards() {
        hand.clear();
        wonCards.clear();
    }

    public Hand hand() {
        return this.hand;
    }

    /**
     * The given card is dealt to this player.
     * OhMyTenses!
     */
    public void deal(Card card) {
        hand.add(card);
    }

    /**
     * This player picks up the given cards from the trick.
     * // TODO we may want to keep track of the various tricks instead of flattening ?
     */
    public void pickup(Set<Card> cards) {
        wonCards.addAll(cards);
    }

    public Set<Card> wonCards() {
        return Collections.unmodifiableSet(wonCards);
    }

    /**
     * The player has successfully played these, we remove them from her hand.
     */
    void discard(Set<Card> cards) {
        hand.discard(cards);
    }

    @Override
    public String toString() {
        return (
            "Player{" +
            "id=" +
            id +
            ", ready=" +
            ready +
            ", hand=" +
            hand +
            ", wonCards=" +
            wonCards +
            '}'
        );
    }

    public static class Hand {

        private final Set<Card> cards = new LinkedHashSet<>();

        void clear() {
            this.cards.clear();
        }

        void add(Card card) {
            this.cards.add(card);
        }

        boolean hasAll(Set<Card> cards) {
            return this.cards.containsAll(cards);
        }

        boolean has(Predicate<Card> test) {
            return this.cards.stream().anyMatch(test);
        }

        public int size() {
            return this.cards.size();
        }

        boolean isEmpty() {
            return this.cards.isEmpty();
        }

        void discard(Set<Card> cards) {
            if (!cards.containsAll(cards)) {
                throw new IllegalStateException(
                    "Could not remove cards " + cards + " from player's hand"
                );
            }
            // The boolean returned by removeAll does not indicate success, but rather that the collection was mutated
            // So it's false if cards is empty, OR if cards contains at least one card in hand, but doesn't validate all were in hand
            this.cards.removeAll(cards);
        }

        public String toDebugString() {
            return this.cards.stream()
                .map(Card::name)
                .collect(Collectors.joining(", "));
        }
    }
}
