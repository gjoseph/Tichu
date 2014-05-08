package net.incongru.tichu.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author gjoseph
 */
public class CardTest {

    @Test
    public void cardNamesAreOkay() {
        assertEquals("Ace of Sword", new Card(Card.CardNumbers.Ace, Card.CardSuit.Sword).name());
        assertEquals("Queen of Star", new Card(Card.CardNumbers.Queen, Card.CardSuit.Star).name());
        assertEquals("7 of Pagoda", new Card(Card.CardNumbers.Seven, Card.CardSuit.Pagoda).name());
        assertEquals("Dragon", new Card(Card.CardSpecials.Dragon, null).name());
    }
}
