package net.incongru.tichu.model;

import com.google.common.collect.Sets;
import net.incongru.tichu.model.plays.ConsecutivePairs;
import net.incongru.tichu.model.plays.FullHouse;
import net.incongru.tichu.model.plays.InvalidPlay;
import net.incongru.tichu.model.plays.Pair;
import net.incongru.tichu.model.plays.Single;
import net.incongru.tichu.model.plays.Straight;
import net.incongru.tichu.model.plays.Triple;
import org.junit.Test;

import static net.incongru.tichu.model.DeckConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

/**
 */
public class TichuRulesTest {

    @Test
    public void bombIsFour7s() {
        final Play four7s = newPlay(Pagoda_7, Sword_7, Jade_7, Star_7);
        final Play three7s = newPlay(Pagoda_7, Sword_7, Jade_7);
        assertEquals("pre-flight", four7s.getCards().size(), 4);
        assertEquals("pre-flight", three7s.getCards().size(), 3);

        assertTrue(new TichuRules().isBomb(four7s));
        assertFalse(new TichuRules().isBomb(three7s));
    }

    @Test
    public void bombIsSameColorStraight() {
        Play sameColor4Straight = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7);
        Play sameColor5Straight = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7, Pagoda_8);
        Play diffColor5Straight = newPlay(Pagoda_4, Star_5, Pagoda_6, Sword_7, Pagoda_8);
        Play sameColor7Straight = newPlay(Pagoda_4, Pagoda_5, Pagoda_6, Pagoda_7, Pagoda_8, Pagoda_9, Pagoda_10);
        final TichuRules rules = new TichuRules();

        assertThat(sameColor5Straight).isInstanceOf(Straight.class);
        assertThat(sameColor7Straight).isInstanceOf(Straight.class);
        assertThat(diffColor5Straight).isInstanceOf(Straight.class);
        assertThat(sameColor4Straight).isInstanceOf(InvalidPlay.class); // This is not a Straight

        assertTrue(rules.isBomb(sameColor5Straight));
        assertTrue(rules.isBomb(sameColor7Straight));
        assertFalse(rules.isBomb(diffColor5Straight));
    }

    @Test
    public void straightIsNotABombWithPhoenix() {
        final Play play = newPlay(Pagoda_4, Pagoda_5, Phoenix, Pagoda_7, Pagoda_8);
        assertThat(play).isInstanceOf(Straight.class);
        assertFalse(play.isBomb());
    }

    @Test
    public void straightProperties() {
        Play p = newPlay(Sword_7, Star_5, Pagoda_6, Pagoda_4, Pagoda_8, Star_3);

        assertThat(p).isInstanceOf(Straight.class);
        Straight straight = (Straight) p;

        // assertThat(straight.getHigherBound(), equalTo(Pagoda_8);
        // assertThat(straight.getLowerBound(), equalTo(Star_3);
        assertThat(straight.getHigherBound()).isEqualTo(Card.CardNumbers.Eight);
        assertThat(straight.getLowerBound()).isEqualTo(Card.CardNumbers.Three);
        assertThat(straight.size()).isEqualTo(6);
        assertThat(straight.name()).isEqualTo("Straight");
        assertThat(straight.describe()).isEqualTo("Straight of 6, from 3 to 8");
    }

    @Test
    public void basicValidStraights() {
        assertThat(newPlay(Star_2, Sword_3, Pagoda_4, Jade_5, Star_6)).isInstanceOf(Straight.class);
        // Start with MahJong (1)
        assertThat(newPlay(MahJong, Star_2, Sword_3, Pagoda_4, Jade_5)).isInstanceOf(Straight.class);
        // End with ace
        assertThat(newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Star_Ace)).isInstanceOf(Straight.class);
    }

    // TODO should something log "hints" about why a play isn't match and possibly return it ?
    @Test
    public void invalidStraights() {
        assertThat(newPlay(Sword_2, Sword_3, Sword_4, Sword_5)).isInstanceOf(InvalidPlay.class).withFailMessage("Not long enough (only 4 cards)");
        assertThat(newPlay(Sword_2, Sword_3, Jade_3, Sword_4, Sword_5, Jade_6)).isInstanceOf(InvalidPlay.class).withFailMessage("Can't have same cards (Sword3 and Jade3)");
        assertThat(newPlay(Sword_2, Sword_3, Sword_4, Star_6, Sword_7)).isInstanceOf(InvalidPlay.class).withFailMessage("Can't skip (*5 is missing)");
    }

    @Test
    public void canNotSubPhoenixForMahjongInStraight() {
        // since our nice Straight factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertThat(newPlay(Phoenix, Sword_2, Sword_3, Sword_4, Sword_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace)).isInstanceOf(InvalidPlay.class).withFailMessage("Can't sub Phoenix for mahjong");
    }

    @Test
    public void canNotUsePhoenixBeforeMahjongInStraight() {
        // since our nice Straight factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertThat(newPlay(Phoenix, MahJong, Sword_2, Sword_3, Sword_4, Sword_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace)).isInstanceOf(InvalidPlay.class).withFailMessage("Can't sub Phoenix for before mahjong");
    }

    @Test
    public void straightStartingWithPhoenixMentionsIt() {
        final Play s = newPlay(Phoenix, Star_Jack, Sword_Queen, Sword_King, Sword_Ace);
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe()).contains("Straight of 5, from 10 to Ace").contains("Phoenix");
        System.out.println("s.describe(): " + s.describe());
    }

    @Test
    public void straightEndingWithPhoenixMentionsIt() {
        final Play s = newPlay(Star_10, Star_Jack, Sword_Queen, Sword_King, Phoenix);
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe()).contains("Straight of 5, from 10 to Ace").contains("Phoenix");
    }

    @Test
    public void straightWithPhoenixMentionsIt() {
        final Play s = newPlay(Star_10, Star_Jack, Phoenix, Sword_King, Star_Ace);
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe()).contains("Straight of 5, from 10 to Ace")
                .contains("Phoenix")
                .contains("for the Queen");
    }

    @Test
    public void phoenixCanSubForAnyInStraightExceptAfterAce() {
        final Play s1 = newPlay(Sword_2, Pagoda_3, Phoenix, Pagoda_5, Pagoda_6);
        final Play s2 = newPlay(Sword_2, Pagoda_3, Pagoda_4, Pagoda_5, Phoenix);
        final Play s3 = newPlay(Phoenix, Sword_2, Pagoda_3, Pagoda_4, Pagoda_5);
        final Play s4 = newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Phoenix);
        assertThat(s1).isInstanceOf(Straight.class);
        assertThat(s2).isInstanceOf(Straight.class);
        assertThat(s3).isInstanceOf(Straight.class);
        assertThat(s4).isInstanceOf(Straight.class);
    }

    @Test
    public void canNotAddPhoenixAfterAceInStraight() {
        // since our nice Straight factory will attempt to place the phoenix before, we add alll possible cards
        final Play s_wrong = newPlay(Star_2, Star_3, Star_4, Star_5, Star_6, Star_7, Star_8, Star_9, Star_10,
                Star_Jack, Star_Queen, Star_King, Star_Ace, Phoenix);
        assertThat(s_wrong).isInstanceOf(InvalidPlay.class);
    }

    @Test
    public void cantPlayTripleAfterPairEtc() {
        final Play single = newPlay(Star_2);
        final Play pair = newPlay(Star_3, Pagoda_3);
        final Play triple = newPlay(Star_4, Pagoda_4, Sword_4);
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

    @Test
    public void invalidPlayIsInvalid() {
        final Play play = newPlay(Sword_2, Pagoda_2, Sword_3);
        assertThat(play).isInstanceOf(InvalidPlay.class);
        assertFalse(new TichuRules().isValid(play));
        assertFalse(new TichuRules().isBomb(play));
    }

    @Test
    public void findBasicTricks() {
        assertThat(newPlay(K2)).isInstanceOf(Single.class);
        assertThat(newPlay(K2, B2)).isInstanceOf(Pair.class);
        assertThat(newPlay(K2, B2, R2)).isInstanceOf(Triple.class);
    }

    @Test
    public void findFullHouse() {
        assertThat(newPlay(K2, B2, R2, R3, B3)).isInstanceOf(FullHouse.class);
    }

    @Test
    public void consecutivePairs() {
        assertThat(newPlay(K2, B2, R3, B3)).isInstanceOf(ConsecutivePairs.class);
        assertThat(newPlay(K2, B2, R4, B4)).isNotInstanceOf(ConsecutivePairs.class);
        assertThat(newPlay(K2, B2, G3, R3, R4, B4)).isInstanceOf(ConsecutivePairs.class);
    }

    @Test
    public void attemptingToUseTheSameCardTwiceInOnePlayIsWorseThanInvalid() {
        final Play before = newPlay(Pagoda_7);
        // TODO : the below should actually return null, it's not a valid play
        final Play twoIdenticalCard = newPlay(Pagoda_8, Pagoda_8);
        // Then this should return false or blow
        assertThatThrownBy(() -> new TichuRules().canPlayAfter(before, twoIdenticalCard)).isInstanceOf(Cheating.class);
        // TODO but it doesn't, since we're passing Set<Card>, in fact newPlay(Pagoda_8, Pagoda_8) returns a Single play.
    }

    private Play newPlay(Card... cards) {
        return new TichuRules().validate(Sets.newHashSet(cards));
    }
}
