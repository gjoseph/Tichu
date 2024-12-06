package net.incongru.tichu.model;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardNumbers;
import net.incongru.tichu.model.card.CardSpecials;
import net.incongru.tichu.model.card.CardSuit;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

/**
 * All cards.
 */
public class CardDeck {

    private final List<Card> cards;

    public CardDeck() {
        final Set<Card> cards = new LinkedHashSet<>();
        // Add CardNumbers
        for (CardNumbers cardNumbers : CardNumbers.values()) {
            for (CardSuit color : CardSuit.values()) {
                cards.add(Card.of(cardNumbers, color));
            }
        }
        // Add CardSpecials
        Arrays.stream(CardSpecials.values()).forEach(s -> cards.add(Card.of(s))
        );

        this.cards = shuffle(cards);
    }

    protected List<Card> shuffle(
        @UnderInitialization CardDeck this,
        Set<Card> cards
    ) {
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
