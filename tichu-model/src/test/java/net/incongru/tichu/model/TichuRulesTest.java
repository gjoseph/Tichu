package net.incongru.tichu.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * @author gjoseph
 */
public class TichuRulesTest {
    private final CardDeck d = new CardDeck();

    @Test
    public void bombIsFourSevens() {

        Trick.Play fourSevens = new Trick.Play(Sets.newHashSet(d.find(Card.CardNumbers.Seven, Card.CardSuit.Pagoda)));
        Trick.Play threeSevens = new Trick.Play(Sets.newHashSet(d.find(Card.CardNumbers.Seven, Card.CardSuit.Pagoda)));

        assertTrue(new TichuRules().isBomb(fourSevens));
        assertFalse(new TichuRules().isBomb(threeSevens));
    }

    @Test
    public void bombIsSameColorStreet() {
        fail("not implemented");
//        assertTrue(new TichuRules().isBomb(sameColor5Street));
//        assertTrue(new TichuRules().isBomb(sameColor7Street));
//        assertFalse(new TichuRules().isBomb(sameColor4Street));
//        assertFalse(new TichuRules().isBomb(diffColor5Street));
    }
}
