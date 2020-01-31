package net.incongru.tichu.simu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Removes and returns the token at index i.
     */
    String pop(int i) {
        return consumedTokens.remove(i);
    }

    /**
     * Compares the token at index i with the given String, and pops it if equals.
     * @return whether the token is equal to the given String
     */
    boolean test(int i, String expectedValue) {
        boolean peek = peek(i).equals(expectedValue);
        if (peek) {
            pop(i);
        }
        return peek;
    }

    /**
     * Returns all unpopped tokens as a String.
     */
    String remainder() {
        return String.join(" ", consumedTokens);
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
