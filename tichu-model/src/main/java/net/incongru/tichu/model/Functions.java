package net.incongru.tichu.model;

import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
class Functions {

    static int score(Collection<Card> cards) {
        return cards.stream().mapToInt(card -> card.getVal().scoreValue()).sum();
    }

    /**
     * returns true if the last N elements of the given Deque match the given predicate.
     */
    static <T> boolean lastNMatches(Deque<T> deque, int n, Predicate<T> test) {
        return Iterators.all(Iterators.limit(deque.descendingIterator(), n), test::test);
    }

    /**
     * returns true if the last N elements of the given List match the given predicate.
     */
    static <T> boolean lastNMatches(List<T> list, int n, java.util.function.Predicate<T> test) {
        return list.subList(list.size() - 3, list.size()).stream().allMatch(test);
    }

    static final Predicate<Player> EMPTY_HANDED = player -> !player.hand().isEmpty();

}
