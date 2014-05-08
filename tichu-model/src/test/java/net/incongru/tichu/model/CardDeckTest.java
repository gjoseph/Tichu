package net.incongru.tichu.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author gjoseph
 */
public class CardDeckTest {
    @Test
    public void canFindSpecials() {
        Card dog = new CardDeck().find(Card.CardSpecials.Dog);
        assertEquals(dog.getVal(), Card.CardSpecials.Dog);
        assertEquals(dog.getVal().isSpecial(), true);
        assertEquals(dog.getVal().playOrder(), 1);
        assertEquals(dog.getVal().scoreValue(), 0);
        assertEquals(null, dog.getSuit());
        assertEquals("Dog", dog.name());
    }

    @Test
    public void canFindNumberCard() {
        Card card = new CardDeck().find(Card.CardNumbers.Eight, Card.CardSuit.Jade);
        assertEquals(false, card.getVal().isSpecial());
        assertEquals(8, card.getVal().playOrder());
        assertEquals(0, card.getVal().scoreValue());
        assertEquals(Card.CardSuit.Jade, card.getSuit());
        assertEquals("8 of Jade", card.name());
    }
}
