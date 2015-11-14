package net.incongru.tichu.model;

import static net.incongru.tichu.model.DeckConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Sets;

import net.incongru.tichu.model.plays.Street;

/**
 * @author gjoseph
 */
public class TichuRulesTest {

    @Test
    public void eachCardIsUniqueSoFuckYouIfYoureTryingToPlayTheSameCardTwiceInTheSameHand() {
        final Trick.Play before = newPlay(Pagoda_7);
        // TODO : the below should actually return null, it's not a valid play
        final Trick.Play twoIdenticalCard = newPlay(Pagoda_8, Pagoda_8);
        // Then this should return false or blow
        assertThatThrownBy(() -> new TichuRules().canPlayAfter(before, twoIdenticalCard)).isInstanceOf(IllegalArgumentException.class);
        // TODO but it doesn't, since we're passing Set<Card>, in fact newPlay(Pagoda_8, Pagoda_8) returns a Single play.
    }

    @Test
    public void bombIsFour7s() {
        final Trick.Play four7s = newPlay(Pagoda_7, Sword_7, Jade_7, Star_7);
        final Trick.Play three7s = newPlay(Pagoda_7, Sword_7, Jade_7);
        assertEquals("pre-flight", four7s.getCards().size(), 4);
        assertEquals("pre-flight", three7s.getCards().size(), 3);

        assertTrue(new TichuRules().isBomb(four7s));
        assertFalse(new TichuRules().isBomb(three7s));
    }

    @Test
    public void bombIsSameColorStreet() {
        Trick.Play sameColor4Street = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7);
        Trick.Play sameColor5Street = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7, Pagoda_8);
        Trick.Play diffColor5Street = newPlay(Pagoda_4, Star_5, Pagoda_6, Sword_7, Pagoda_8);
        Trick.Play sameColor7Street = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7, Pagoda_8, Pagoda_9, Pagoda_10);
        final TichuRules rules = new TichuRules();

        assertThat(sameColor5Street).isInstanceOf(Street.class);
        assertThat(sameColor7Street).isInstanceOf(Street.class);
        assertThat(diffColor5Street).isInstanceOf(Street.class);
        assertThat(sameColor4Street).isNull(); // This is not a Street

        assertTrue(rules.isBomb(sameColor5Street));
        assertTrue(rules.isBomb(sameColor7Street));
        assertFalse(rules.isBomb(diffColor5Street));
    }

    @Test
    public void streetIsNotABombWithPhoenix() {
        final Trick.Play play = newPlay(Pagoda_4, Pagoda_5, Phoenix, Pagoda_7, Pagoda_8);
        assertThat(play).isInstanceOf(Street.class);
        assertFalse(play.isBomb());
    }

    @Test
    public void streetProperties() {
        Trick.Play p = newPlay(Sword_7, Star_5, Pagoda_6, Pagoda_4, Pagoda_8, Star_3);

        assertThat(p).isInstanceOf(Street.class);
        Street street = (Street) p;

        // assertThat(street.getHigherBound(), equalTo(Pagoda_8);
        // assertThat(street.getLowerBound(), equalTo(Star_3);
        assertThat(street.getHigherBound()).isEqualTo(Card.CardNumbers.Eight);
        assertThat(street.getLowerBound()).isEqualTo(Card.CardNumbers.Three);
        assertThat(street.size()).isEqualTo(6);
        assertThat(street.name()).isEqualTo("Street");
        assertThat(street.describe()).isEqualTo("Street of 6, from 3 to 8");
    }

    @Test
    public void basicValidStreets() {
        assertThat(newPlay(Star_2, Sword_3, Pagoda_4, Jade_5, Star_6)).isInstanceOf(Street.class);
        // Start with MahJong (1)
        assertThat(newPlay(MahJong, Star_2, Sword_3, Pagoda_4, Jade_5)).isInstanceOf(Street.class);
        // End with ace
        assertThat(newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Star_Ace)).isInstanceOf(Street.class);
    }

    // TODO should something log "hints" about why a play isn't match and possibly return it ?
    @Test
    public void invalidStreets() {
        assertNull("Not long enough (only 4 cards)", newPlay(Sword_2, Sword_3, Sword_4, Sword_5));
        assertNull("Can't have same cards (Sword3 and Jade3)", newPlay(Sword_2, Sword_3, Jade_3, Sword_4, Sword_5, Jade_6));
        assertNull("Can't skip (*5 is missing)", newPlay(Sword_2, Sword_3, Sword_4, Star_6, Sword_7));
    }

    @Test
    public void canNotSubPhoenixForMahjongInStreet() {
        // since our nice Street factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertNull("Can't sub Phoenix for mahjong", newPlay(Phoenix, Sword_2, Sword_3, Sword_4, Sword_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace));
    }

    @Test
    public void canNotUsePhoenixBeforeMahjongInStreet() {
        // since our nice Street factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertNull("Can't sub Phoenix for before mahjong", newPlay(Phoenix, MahJong, Sword_2, Sword_3, Sword_4, Sword_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace));
    }


    @Test
    public void streetStartingWithPhoenixMentionsIt() {
        final Trick.Play s = newPlay(Phoenix, Star_Jack, Sword_Queen, Sword_King, Sword_Ace);
        assertThat(s).isInstanceOf(Street.class);
        assertThat(s.describe()).contains("Street of 5, from 10 to Ace").contains("Phoenix");
        System.out.println("s.describe(): " + s.describe());
    }

    @Test
    public void streetEndingWithPhoenixMentionsIt() {
        final Trick.Play s = newPlay(Star_10, Star_Jack, Sword_Queen, Sword_King, Phoenix);
        assertThat(s).isInstanceOf(Street.class);
        assertThat(s.describe()).contains("Street of 5, from 10 to Ace").contains("Phoenix");
    }

    @Test
    public void streetWithPhoenixMentionsIt() {
        final Trick.Play s = newPlay(Star_10, Star_Jack, Phoenix, Sword_King, Star_Ace);
        assertThat(s).isInstanceOf(Street.class);
        assertThat(s.describe()).contains("Street of 5, from 10 to Ace")
                .contains("Phoenix")
                .contains("for the Queen");
    }

    @Test
    public void phoenixCanSubForAnyInStreetExceptAfterAce() {
        final Trick.Play s1 = newPlay(Sword_2, Pagoda_3, Phoenix, Pagoda_5, Pagoda_6);
        final Trick.Play s2 = newPlay(Sword_2, Pagoda_3, Pagoda_4, Pagoda_5, Phoenix);
        final Trick.Play s3 = newPlay(Phoenix, Sword_2, Pagoda_3, Pagoda_4, Pagoda_5);
        final Trick.Play s4 = newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Phoenix);
        assertThat(s1).isInstanceOf(Street.class);
        assertThat(s2).isInstanceOf(Street.class);
        assertThat(s3).isInstanceOf(Street.class);
        assertThat(s4).isInstanceOf(Street.class);
    }

    @Test
    public void canNotAddPhoenixAfterAceInStreet() {
        // since our nice Street factory will attempt to place the phoenix before, we add alll possible cards
        final Trick.Play s_wrong = newPlay(Star_2, Star_3, Star_4, Star_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace, Phoenix);
        assertThat(s_wrong).isNull();

    }

    @Test
    public void cantPlayTripleAfterPairEtc() {
        final Trick.Play single = newPlay(Star_2);
        final Trick.Play pair = newPlay(Star_3, Pagoda_3);
        final Trick.Play triple = newPlay(Star_4, Pagoda_4, Sword_4);
        final TichuRules rules = new TichuRules();
        assertFalse(rules.canPlayAfter(pair, triple));
        assertFalse(rules.canPlayAfter(single, pair));
    }

    @Test
    public void playOrderWithSingles() {
        final TichuRules rules = new TichuRules();
        assertTrue(rules.canPlayAfter(newPlay(Pagoda_2), newPlay(Star_7)));
        assertFalse(rules.canPlayAfter(newPlay(Star_3), newPlay(Pagoda_2)));
        assertFalse("next play must be strictly higher", rules.canPlayAfter(newPlay(Star_2), newPlay(Pagoda_2)));
    }

    @Test
    public void playOrderWithPairs() {
        final TichuRules rules = new TichuRules();
        assertTrue(rules.canPlayAfter(newPlay(Pagoda_2, Star_2), newPlay(Sword_7, Star_7)));
        assertFalse(rules.canPlayAfter(newPlay(Star_3, Sword_3), newPlay(Pagoda_2, Star_2)));
        assertFalse("next pair must be strictly higher", rules.canPlayAfter(newPlay(Star_2, Sword_2), newPlay(Pagoda_2, Jade_2)));
    }

    private Trick.Play newPlay(Card... cards) {
        return new TichuRules().validate(Sets.newHashSet(cards));
    }
}
