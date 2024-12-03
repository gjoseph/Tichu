package net.incongru.tichu.model;

import static net.incongru.tichu.model.util.DeckConstants.B0;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.B5;
import static net.incongru.tichu.model.util.DeckConstants.B9;
import static net.incongru.tichu.model.util.DeckConstants.BK;
import static net.incongru.tichu.model.util.DeckConstants.G5;
import static net.incongru.tichu.model.util.DeckConstants.R5;
import static net.incongru.tichu.model.util.DeckConstants._D;
import static net.incongru.tichu.model.util.DeckConstants._P;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 */
class FunctionsTest {

    @Test
    void scoringDoesNotRelyOnOrder() {
        assertEquals(25, Functions.score(Arrays.asList(B2, B0, B5, B9, BK)));
    }

    @Test
    void scoreCanDealWithNegatives() {
        assertEquals(-5, Functions.score(Arrays.asList(_P, B0, G5, R5)));
        assertEquals(5, Functions.score(Arrays.asList(_D, _P, R5)));
    }

    @Test
    void lastNMatchWithDeque() {
        Deque<Integer> dq = new LinkedList<>();
        dq.add(1);
        dq.add(2);
        dq.add(3);
        dq.add(4);
        dq.add(5);
        dq.add(6);

        assertThat(Functions.lastNMatches(dq, 3, i -> i > 3)).isTrue();
        assertThat(Functions.lastNMatches(dq, 3, i -> i > 5)).isFalse();

        // LinkedList is both Deque and List, so cast
        assertThat(
            Functions.lastNMatches((List<Integer>) dq, 3, i -> i > 3)
        ).isTrue();
        assertThat(
            Functions.lastNMatches((List<Integer>) dq, 3, i -> i > 5)
        ).isFalse();
    }
}
