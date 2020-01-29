package net.incongru.tichu.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class Functions {

    // TODO are the find methods useful outside tests ? Should they remove card from deck ?
    public static Optional<Card> find(Collection<Card> cards, Card.CardSpecials value) {
        return cards.stream().filter(c -> is(c, value)).findFirst();
    }

    // TODO are the find methods useful outside tests ? Should they remove card from deck ?
    public static Optional<Card> find(Collection<Card> cards, Card.CardNumbers value, Card.CardSuit suit) {
        return cards.stream().filter(c -> c.getVal() == value && c.getSuit() == suit).findFirst();
    }

    public static boolean is(Card c, Card.CardNumbers value, Card.CardSuit suit) {
        return c.getVal() == value && c.getSuit() == suit;
    }

    public static boolean is(Card c, Card.CardSpecials value) {
        return c.getVal() == value;
    }

    public static int score(Collection<Card> cards) {
        return cards.stream().mapToInt(card -> card.getVal().scoreValue()).sum();
    }

    /**
     * returns true if the last N elements of the given Deque match the given predicate.
     */
    public static <T> boolean lastNMatches(Deque<T> deque, int n, Predicate<T> test) {
        return Iterators.all(Iterators.limit(deque.descendingIterator(), n), test);
    }

    /**
     * returns true if the last N elements of the given List match the given predicate.
     */
    public static <T> boolean lastNMatches(List<T> list, int n, java.util.function.Predicate<T> test) {
        return list.subList(list.size() - 3, list.size()).stream().allMatch(test);
    }

    public static final java.util.function.Predicate<Players.Player> EMPTY_HANDED = player -> !player.hand().isEmpty();

}
