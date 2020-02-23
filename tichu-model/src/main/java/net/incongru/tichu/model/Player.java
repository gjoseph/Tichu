package net.incongru.tichu.model;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
public class Player {
    private final String name;
    private boolean ready = false;
    private final Set<Card> hand = new LinkedHashSet<>();
    private final Set<Card> wonCards = new LinkedHashSet<>();

    public Player(String name) {
        Preconditions.checkNotNull(name, "Player name can't be null");
        this.name = name;
        this.ready = false;
    }

    public String name() {
        return name;
    }

    public void setReady() {
        if (this.ready) {
            throw new IllegalStateException(name() + " is already ready");
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

    public Set<Card> hand() {
        return Collections.unmodifiableSet(hand);
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
        if (!hand.containsAll(cards)) {
            throw new IllegalStateException("Could not remove cards " + cards + " from player hand " + hand);
        }
        // The boolean returned by removeAll does not indicate success, but rather that the collection was mutated
        // So it's false if cards is empty, OR if cards contains at least one card in hand, but doesn't validate all were in hand
        hand.removeAll(cards);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                ", wonCards=" + wonCards +
                '}';
    }
}
