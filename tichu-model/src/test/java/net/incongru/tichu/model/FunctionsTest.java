package net.incongru.tichu.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static net.incongru.tichu.model.DeckConstants.B0;
import static net.incongru.tichu.model.DeckConstants.B2;
import static net.incongru.tichu.model.DeckConstants.B5;
import static net.incongru.tichu.model.DeckConstants.B9;
import static net.incongru.tichu.model.DeckConstants.BK;
import static net.incongru.tichu.model.DeckConstants.G5;
import static net.incongru.tichu.model.DeckConstants.R5;
import static net.incongru.tichu.model.DeckConstants._D;
import static net.incongru.tichu.model.DeckConstants._P;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class FunctionsTest {
    @Test
    public void canFindSpecials() {
        Card dog = Functions.find(new CardDeck().allRemaining(), Card.CardSpecials.Dog).get();
        assertEquals(dog.getVal(), Card.CardSpecials.Dog);
        assertEquals(dog.getVal().isSpecial(), true);
        assertEquals(dog.getVal().playOrder(), 1);
        assertEquals(dog.getVal().scoreValue(), 0);
        assertEquals(null, dog.getSuit());
        assertEquals("Dog", dog.name());
    }

    @Test
    public void canFindNumberCard() {
        Card card = Functions.find(new CardDeck().allRemaining(), Card.CardNumbers.Eight, Card.CardSuit.Jade).get();
        assertEquals(false, card.getVal().isSpecial());
        assertEquals(8, card.getVal().playOrder());
        assertEquals(0, card.getVal().scoreValue());
        assertEquals(Card.CardSuit.Jade, card.getSuit());
        assertEquals("8 of Jade", card.name());
    }

    @Test
    public void scoringDoesNotRelyOnOrder() {
        assertEquals(25, Functions.score(Arrays.asList(B2, B0, B5, B9, BK)));
    }

    @Test
    public void scoreCanDealWithNegatives() {
        assertEquals(-5, Functions.score(Arrays.asList(_P, B0, G5, R5)));
        assertEquals(5, Functions.score(Arrays.asList(_D, _P, R5)));
    }

    @Test
    public void lastNMatchWithDeque() {
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
        assertThat(Functions.lastNMatches((List<Integer>) dq, 3, i -> i > 3)).isTrue();
        assertThat(Functions.lastNMatches((List<Integer>) dq, 3, i -> i > 5)).isFalse();
    }
}
