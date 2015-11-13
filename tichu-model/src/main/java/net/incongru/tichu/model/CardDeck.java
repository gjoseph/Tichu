package net.incongru.tichu.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.Iterables;

/**
 * All cards.
 */
public class CardDeck {
    private Set<Card> cards;

    public CardDeck() {
        cards = new LinkedHashSet<>();
        for (Card.CardNumbers cardNumbers : Card.CardNumbers.values()) {
            for (Card.CardSuit color : Card.CardSuit.values()) {
                cards.add(new Card(cardNumbers, color));
            }
        }
        // Arrays.asList(Card.CardSpecials.values()).forEach(s -> {new Card(s,null);});
        for (Card.CardSpecials cardSpecials : Card.CardSpecials.values()) {
            cards.add(new Card(cardSpecials, null));
        }
    }

    public Set<Card> getCards() {
        return cards;
    }

    // TODO are the find methods useful outside tests ? Should they remove card from deck ?
    public Card find(Card.CardSpecials value) {
        return Iterables.find(cards, c -> c.getVal() == value);
    }

    // TODO are the find methods useful outside tests ? Should they remove card from deck ?
    public Card find(Card.CardNumbers value, Card.CardSuit suit) {
        return Iterables.find(cards, c -> c.getVal() == value && c.getSuit() == suit);
    }
}
