package net.incongru.tichu.model;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * All cards.
 */
public class CardDeck {

    private final List<Card> cards;

    public CardDeck() {
        final Set<Card> cards = new LinkedHashSet<>();
        // Add CardNumbers
        for (Card.CardNumbers cardNumbers : Card.CardNumbers.values()) {
            for (Card.CardSuit color : Card.CardSuit.values()) {
                cards.add(new Card(cardNumbers, color));
            }
        }
        // Add CardSpecials
        Arrays.stream(Card.CardSpecials.values()).forEach(s -> cards.add(new Card(s, null)));

        this.cards = shuffle(cards);
    }

    protected List<Card> shuffle(Set<Card> cards) {
        final ArrayList<Card> list = new ArrayList<>(cards);
        Collections.shuffle(list);
        return list;
    }

    public Card take() {
        return cards.remove(0);
    }

    public Set<Card> allRemaining() {
        return ImmutableSet.copyOf(cards);
    }

    public boolean isComplete() {
        return cards.size() == 56;
    }
}
