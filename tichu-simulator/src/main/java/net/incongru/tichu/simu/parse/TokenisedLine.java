package net.incongru.tichu.simu.parse;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TokenisedLine {
    private final List<String> originalTokens;
    private List<String> consumedTokens;

    public TokenisedLine(String line) {
        this.originalTokens = new ArrayList<>(Arrays.asList(line.split("\\s+")));
        reset();
    }

    /**
     * Returns the token at index i.
     */
    String peek(int i) {
        return consumedTokens.get(i);
    }

    /**
     * Compares the token at index i with the given String, and returns true if it is equals, without popping it.
     */
    boolean peek(int i, String expectedValue) {
        return peek(i, s -> s.equals(expectedValue)).isPresent();
    }

    /**
     * Tests the token at index i with the given Predicate, and returns it if it matches, without popping it.
     *
     * @return the matched value value, if any, or an empty Optional otherwise
     */
    Optional<String> peek(int i, Predicate<String> predicate) {
        return i < count() && predicate.test(peek(i)) ? Optional.of(peek(i)) : Optional.empty();
    }

    /**
     * Removes and returns the token at index i.
     */
    String pop(int i) {
        return consumedTokens.remove(i);
    }

    int popInt(int i) {
        try {
            return Integer.parseInt(pop(i));
        } catch (NumberFormatException e) {
            throw new LineParserException(this, e.getMessage());
        }
    }

    /**
     * Compares the token at index i with the given String, and pops it if equals.
     *
     * @return whether the token is equal to the given String
     */
    boolean test(int i, String expectedValue) {
        return test(i, s -> s.equals(expectedValue)).isPresent();
    }

    /**
     * Tests the token at index i with the given Predicate, and pops it if it matches.
     *
     * @return the popped value, if any, or an empty Optional otherwise
     */
    Optional<String> test(int i, Predicate<String> predicate) {
        return i < count() && predicate.test(peek(i)) ? Optional.of(pop(i)) : Optional.empty();
    }

    /**
     * Returns count of unpopped tokens.
     */
    int count() {
        return consumedTokens.size();
    }

    /**
     * Returns all unpopped tokens as a String.
     */
    String remainder() {
        return String.join(" ", consumedTokens);
    }

    Set<Card> remainderAsCards() {
        final String cardsStr = remainder();
        return Arrays.stream(cardsStr.split("\\s*,\\s*"))
                .map(DeckConstants::byName)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all tokens as a String.
     */
    String whole() {
        return String.join(" ", originalTokens);
    }

    /**
     * Resets popped tokens to original values.
     */
    TokenisedLine reset() {
        this.consumedTokens = new ArrayList<>(this.originalTokens);
        return this;
    }
}
