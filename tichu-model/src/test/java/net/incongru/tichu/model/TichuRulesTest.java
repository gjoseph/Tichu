package net.incongru.tichu.model;

import static net.incongru.tichu.model.DeckConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

import org.junit.Test;

import com.google.common.collect.Sets;

import net.incongru.tichu.model.plays.Street;

/**
 * @author gjoseph
 */
public class TichuRulesTest {

    @Test(expected = IllegalArgumentException.class)
    public void eachCardIsUniqueSoFuckYouIfYoureTryingToPlayTheSameCardTwiceInTheSameHand() {
        final Trick.Play before = newPlay(Pagoda_7);
        // TODO : the below should actually return null, it's not a valid play
        final Trick.Play twoIdenticalCard = newPlay(Pagoda_8, Pagoda_8);
        // Then this should return false or blow
        new TichuRules().canPlayAfter(before, twoIdenticalCard);
        // TODO but it doesn't, since we're passing Set<Card>, in fact newPlay(Pagoda_8, Pagoda_8) returns a Single play.
    }

    @Test
    public void bombIsFour7s() {
        final Trick.Play four7s = newPlay(Pagoda_7, Sword_7, Jade_7, Star_7);
        final Trick.Play three7s = newPlay(Pagoda_7, Sword_7, Jade_7);
        assumeThat(four7s.getCards().size(), equalTo(4));
        assumeThat(three7s.getCards().size(), equalTo(3));

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
        // assertThat(Arrays.asList(sameColor5Street, diffColor5Street, sameColor7Street), everyItem(instanceOf(Street.class)));
        assertThat(sameColor5Street, instanceOf(Street.class));
        assertThat(sameColor7Street, instanceOf(Street.class));
        assertThat(diffColor5Street, instanceOf(Street.class));
        assertThat(sameColor4Street, nullValue());

        assertTrue(rules.isBomb(sameColor5Street));
        assertTrue(rules.isBomb(sameColor7Street));
        assertFalse(rules.isBomb(sameColor4Street));
        assertFalse(rules.isBomb(diffColor5Street));
    }

    @Test
    public void streetIsNotABombWithPhoenix() {
        final Trick.Play play = newPlay(Pagoda_4, Pagoda_5, Phoenix, Pagoda_7, Pagoda_8);
        assertThat(play, instanceOf(Street.class));
        assertFalse(play.isBomb());
    }

    @Test
    public void streetProperties() {
        Trick.Play p = newPlay(Sword_7, Star_5, Pagoda_6, Pagoda_4, Pagoda_8, Star_3);

        assertThat(p, instanceOf(Street.class));
        Street street = (Street) p;

        // assertThat(street.getHigherBound(), equalTo(Pagoda_8));
        // assertThat(street.getLowerBound(), equalTo(Star_3));
        assertThat(street.getHigherBound(), equalTo(Card.CardNumbers.Eight));
        assertThat(street.getLowerBound(), equalTo(Card.CardNumbers.Three));
        assertThat(street.size(), equalTo(6));
        assertThat(street.name(), equalTo("Street"));
        assertThat(street.describe(), equalTo("Street of 6, from 3 to 8"));
    }

    @Test
    public void basicValidStreets() {
        assertThat(newPlay(Star_2, Sword_3, Pagoda_4, Jade_5, Star_6), instanceOf(Street.class));
        // Start with MahJong (1)
        assertThat(newPlay(MahJong, Star_2, Sword_3, Pagoda_4, Jade_5), instanceOf(Street.class));
        // End with ace
        assertThat(newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Star_Ace), instanceOf(Street.class));
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
        assertThat(s, instanceOf(Street.class));
        assertThat(s.describe(), allOf(
                containsString("Street of 5, from 10 to Ace"),
                containsString("Phoenix")
        ));
        System.out.println("s.describe(): " + s.describe());
    }

    @Test
    public void streetEndingWithPhoenixMentionsIt() {
        final Trick.Play s = newPlay(Star_10, Star_Jack, Sword_Queen, Sword_King, Phoenix);
        assertThat(s, instanceOf(Street.class));
        assertThat(s.describe(), allOf(
                containsString("Street of 5, from 10 to Ace"),
                containsString("Phoenix")
        ));
    }

    @Test
    public void streetWithPhoenixMentionsIt() {
        final Trick.Play s = newPlay(Star_10, Star_Jack, Phoenix, Sword_King, Star_Ace);
        assertThat(s, instanceOf(Street.class));
        assertThat(s.describe(), allOf(
                containsString("Street of 5, from 10 to Ace"),
                containsString("Phoenix"),
                containsString("for the Queen")
        ));
    }

    @Test
    public void phoenixCanSubForAnyInStreetExceptAfterAce() {
        final Trick.Play s1 = newPlay(Sword_2, Pagoda_3, Phoenix, Pagoda_5, Pagoda_6);
        final Trick.Play s2 = newPlay(Sword_2, Pagoda_3, Pagoda_4, Pagoda_5, Phoenix);
        final Trick.Play s3 = newPlay(Phoenix, Sword_2, Pagoda_3, Pagoda_4, Pagoda_5);
        final Trick.Play s4 = newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Phoenix);
        assertThat(s1, instanceOf(Street.class));
        assertThat(s2, instanceOf(Street.class));
        assertThat(s3, instanceOf(Street.class));
        assertThat(s4, instanceOf(Street.class));
    }

    @Test
    public void canNotAddPhoenixAfterAceInStreet() {
        // since our nice Street factory will attempt to place the phoenix before, we add alll possible cards
        final Trick.Play s_wrong = newPlay(Star_2, Star_3, Star_4, Star_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace, Phoenix);
        assertThat(s_wrong, allOf(not(instanceOf(Street.class)), nullValue()));

    }

    @Test
    public void genericsWithCanPlay() {
        final Trick.Play pair = newPlay(Star_2, Pagoda_2);
        final Trick.Play triple = newPlay(Star_3, Pagoda_3, Sword_3);
        // TODO this is because canPlayAfter relies on generics, doesn't type check
        assertFalse(new TichuRules().canPlayAfter(pair, triple));
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
