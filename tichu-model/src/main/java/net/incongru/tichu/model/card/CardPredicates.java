package net.incongru.tichu.model.card;

import java.util.function.Predicate;

public class CardPredicates {
    public static Predicate<Card> is(CardSpecials value) {
        return c -> c.val() == value;
    }

    public static Predicate<Card> is(CardNumbers value, CardSuit suit) {
        return c -> c.val() == value && c.suit() == suit;
    }
}
