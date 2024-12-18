package net.incongru.tichu.model;

import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.B3;
import static net.incongru.tichu.model.util.DeckConstants.B4;
import static net.incongru.tichu.model.util.DeckConstants.Dog;
import static net.incongru.tichu.model.util.DeckConstants.G3;
import static net.incongru.tichu.model.util.DeckConstants.Jade_2;
import static net.incongru.tichu.model.util.DeckConstants.Jade_3;
import static net.incongru.tichu.model.util.DeckConstants.Jade_5;
import static net.incongru.tichu.model.util.DeckConstants.Jade_6;
import static net.incongru.tichu.model.util.DeckConstants.Jade_7;
import static net.incongru.tichu.model.util.DeckConstants.K2;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_10;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_2;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_3;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_4;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_5;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_6;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_7;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_8;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_9;
import static net.incongru.tichu.model.util.DeckConstants.Phoenix;
import static net.incongru.tichu.model.util.DeckConstants.R2;
import static net.incongru.tichu.model.util.DeckConstants.R3;
import static net.incongru.tichu.model.util.DeckConstants.R4;
import static net.incongru.tichu.model.util.DeckConstants.R6;
import static net.incongru.tichu.model.util.DeckConstants.Star_10;
import static net.incongru.tichu.model.util.DeckConstants.Star_2;
import static net.incongru.tichu.model.util.DeckConstants.Star_3;
import static net.incongru.tichu.model.util.DeckConstants.Star_4;
import static net.incongru.tichu.model.util.DeckConstants.Star_5;
import static net.incongru.tichu.model.util.DeckConstants.Star_6;
import static net.incongru.tichu.model.util.DeckConstants.Star_7;
import static net.incongru.tichu.model.util.DeckConstants.Star_8;
import static net.incongru.tichu.model.util.DeckConstants.Star_9;
import static net.incongru.tichu.model.util.DeckConstants.Star_Ace;
import static net.incongru.tichu.model.util.DeckConstants.Star_Jack;
import static net.incongru.tichu.model.util.DeckConstants.Star_King;
import static net.incongru.tichu.model.util.DeckConstants.Star_Queen;
import static net.incongru.tichu.model.util.DeckConstants.Sword_2;
import static net.incongru.tichu.model.util.DeckConstants.Sword_3;
import static net.incongru.tichu.model.util.DeckConstants.Sword_4;
import static net.incongru.tichu.model.util.DeckConstants.Sword_5;
import static net.incongru.tichu.model.util.DeckConstants.Sword_7;
import static net.incongru.tichu.model.util.DeckConstants.Sword_Ace;
import static net.incongru.tichu.model.util.DeckConstants.Sword_King;
import static net.incongru.tichu.model.util.DeckConstants.Sword_Queen;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardNumbers;
import net.incongru.tichu.model.plays.ConsecutivePairs;
import net.incongru.tichu.model.plays.FullHouse;
import net.incongru.tichu.model.plays.Initial;
import net.incongru.tichu.model.plays.InvalidPlay;
import net.incongru.tichu.model.plays.Pair;
import net.incongru.tichu.model.plays.Single;
import net.incongru.tichu.model.plays.Straight;
import net.incongru.tichu.model.plays.Triple;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 */
class TichuRulesTest {

    @Test
    void bombIsFour7s() {
        final Play four7s = newPlay(Pagoda_7, Sword_7, Jade_7, Star_7);
        final Play three7s = newPlay(Pagoda_7, Sword_7, Jade_7);
        assertThat(four7s.getCards())
            .withFailMessage("pre-flight check")
            .hasSize(4);
        assertThat(three7s.getCards())
            .withFailMessage("pre-flight check")
            .hasSize(3);

        assertTrue(new TichuRules().isBomb(four7s));
        assertFalse(new TichuRules().isBomb(three7s));
    }

    @Test
    void bombIsSameColorStraight() {
        Play sameColor4Straight = newPlay(
            Pagoda_4,
            Pagoda_5,
            Pagoda_6,
            Pagoda_7
        );
        Play sameColor5Straight = newPlay(
            Pagoda_4,
            Pagoda_5,
            Pagoda_6,
            Pagoda_7,
            Pagoda_8
        );
        Play diffColor5Straight = newPlay(
            Pagoda_4,
            Star_5,
            Pagoda_6,
            Sword_7,
            Pagoda_8
        );
        Play sameColor7Straight = newPlay(
            Pagoda_4,
            Pagoda_5,
            Pagoda_6,
            Pagoda_7,
            Pagoda_8,
            Pagoda_9,
            Pagoda_10
        );
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
    void straightIsNotABombWithPhoenix() {
        final Play play = newPlay(
            Pagoda_4,
            Pagoda_5,
            Phoenix,
            Pagoda_7,
            Pagoda_8
        );
        assertThat(play).isInstanceOf(Straight.class);
        assertFalse(play.isBomb());
    }

    @Test
    void straightProperties() {
        Play p = newPlay(Sword_7, Star_5, Pagoda_6, Pagoda_4, Pagoda_8, Star_3);

        assertThat(p).isInstanceOf(Straight.class);
        Straight straight = (Straight) p;

        // assertThat(straight.getHigherBound(), equalTo(Pagoda_8);
        // assertThat(straight.getLowerBound(), equalTo(Star_3);
        assertThat(straight.getHigherBound()).isEqualTo(CardNumbers.Eight);
        assertThat(straight.getLowerBound()).isEqualTo(CardNumbers.Three);
        assertThat(straight.size()).isEqualTo(6);
        assertThat(straight.name()).isEqualTo("Straight");
        assertThat(straight.describe()).isEqualTo("Straight of 6, from 3 to 8");
    }

    @Test
    void basicValidStraights() {
        assertThat(
            newPlay(Star_2, Sword_3, Pagoda_4, Jade_5, Star_6)
        ).isInstanceOf(Straight.class);
        // Start with MahJong (1)
        assertThat(
            newPlay(MahJong, Star_2, Sword_3, Pagoda_4, Jade_5)
        ).isInstanceOf(Straight.class);
        // End with ace
        assertThat(
            newPlay(Star_10, Star_Jack, Star_Queen, Star_King, Star_Ace)
        ).isInstanceOf(Straight.class);
    }

    // TODO should something log "hints" about why a play isn't match and possibly return it ?
    @Test
    void invalidStraights() {
        assertThat(newPlay(Sword_2, Sword_3, Sword_4, Sword_5))
            .withFailMessage(
                "Should have been invalid play -- not long enough (only 4 cards)"
            )
            .isInstanceOf(InvalidPlay.class);
        assertThat(newPlay(Sword_2, Sword_3, Jade_3, Sword_4, Sword_5, Jade_6))
            .withFailMessage(
                "Should have been invalid play -- can't have same cards (Sword3 and Jade3)"
            )
            .isInstanceOf(InvalidPlay.class);
        assertThat(newPlay(Sword_2, Sword_3, Sword_4, Star_6, Sword_7))
            .withFailMessage(
                "Should have been invalid play -- can't skip (*5 is missing)"
            )
            .isInstanceOf(InvalidPlay.class);
    }

    @Test
    void canNotSubPhoenixForMahjongInStraight() {
        // since our nice Straight factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertThat(
            newPlay(
                Phoenix,
                Sword_2,
                Sword_3,
                Sword_4,
                Sword_5,
                Star_6,
                Star_7,
                Star_8,
                Star_9,
                Star_10,
                Star_Jack,
                Star_Queen,
                Star_King,
                Star_Ace
            )
        )
            .withFailMessage("Can't sub Phoenix for mahjong")
            .isInstanceOf(InvalidPlay.class);
    }

    @Test
    void canNotUsePhoenixBeforeMahjongInStraight() {
        // since our nice Straight factory will attempt to place the phoenix as high as possible, we add alll possible cards
        assertThat(
            newPlay(
                Phoenix,
                MahJong,
                Sword_2,
                Sword_3,
                Sword_4,
                Sword_5,
                Star_6,
                Star_7,
                Star_8,
                Star_9,
                Star_10,
                Star_Jack,
                Star_Queen,
                Star_King,
                Star_Ace
            )
        )
            .withFailMessage("Can't sub Phoenix for before mahjong")
            .isInstanceOf(InvalidPlay.class);
    }

    @Test
    void straightStartingWithPhoenixMentionsIt() {
        final Play s = newPlay(
            Phoenix,
            Star_Jack,
            Sword_Queen,
            Sword_King,
            Sword_Ace
        );
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe())
            .contains("Straight of 5, from 10 to Ace")
            .contains("Phoenix")
            .contains("for the 10");
    }

    @Test
    void straightEndingWithPhoenixMentionsIt() {
        final Play s = newPlay(
            Star_10,
            Star_Jack,
            Sword_Queen,
            Sword_King,
            Phoenix
        );
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe())
            .contains("Straight of 5, from 10 to Ace")
            .contains("Phoenix")
            .contains("for the Ace");
    }

    @Test
    void straightWithPhoenixMentionsIt() {
        final Play s = newPlay(
            Star_10,
            Star_Jack,
            Phoenix,
            Sword_King,
            Star_Ace
        );
        assertThat(s).isInstanceOf(Straight.class);
        assertThat(s.describe())
            .contains("Straight of 5, from 10 to Ace")
            .contains("Phoenix")
            .contains("for the Queen");
    }

    @Test
    void phoenixCanSubForAnyInStraightExceptAfterAce() {
        final Play s1 = newPlay(Sword_2, Pagoda_3, Phoenix, Pagoda_5, Pagoda_6);
        final Play s2 = newPlay(Sword_2, Pagoda_3, Pagoda_4, Pagoda_5, Phoenix);
        final Play s3 = newPlay(Phoenix, Sword_2, Pagoda_3, Pagoda_4, Pagoda_5);
        final Play s4 = newPlay(
            Star_10,
            Star_Jack,
            Star_Queen,
            Star_King,
            Phoenix
        );
        assertThat(s1).isInstanceOf(Straight.class);
        assertThat(s2).isInstanceOf(Straight.class);
        assertThat(s3).isInstanceOf(Straight.class);
        assertThat(s4).isInstanceOf(Straight.class);
    }

    @Test
    void canNotAddPhoenixAfterAceInStraight() {
        // since our nice Straight factory will attempt to place the phoenix before, we add alll possible cards
        final Play s_wrong = newPlay(
            Star_2,
            Star_3,
            Star_4,
            Star_5,
            Star_6,
            Star_7,
            Star_8,
            Star_9,
            Star_10,
            Star_Jack,
            Star_Queen,
            Star_King,
            Star_Ace,
            Phoenix
        );
        assertThat(s_wrong).isInstanceOf(InvalidPlay.class);
    }

    @Test
    void dogOnlyValidAsSingleCard() {
        // there is a vague possibility that dog and mahjong could be paired since they share some "value"
        assertThat(newPlay(Dog, MahJong)).isInstanceOf(InvalidPlay.class);
    }

    @Test
    void dogCanOnlyBePlayedFirst() {
        final Play singleDog = newPlay(Dog);
        final Play singleMahJong = newPlay(MahJong);
        final TichuRules rules = new TichuRules();
        assertFalse(rules.canPlayAfter(singleMahJong, singleDog));
        assertFalse(rules.canPlayAfter(singleDog, singleMahJong));
        assertTrue(rules.canPlayAfter(Initial.INSTANCE, singleDog));
    }

    @Test
    void dogCanNotBeBombed() {
        final Play singleDog = newPlay(Dog);
        final Play bomb = newPlay(Pagoda_7, Sword_7, Jade_7, Star_7);
        final TichuRules rules = new TichuRules();
        assertFalse(rules.canPlayAfter(singleDog, bomb));
    }

    @Test
    void cantPlayTripleAfterPairEtc() {
        final Play single = newPlay(Star_2);
        final Play pair = newPlay(Star_3, Pagoda_3);
        final Play triple = newPlay(Star_4, Pagoda_4, Sword_4);
        final TichuRules rules = new TichuRules();
        assertFalse(rules.canPlayAfter(pair, triple));
        assertFalse(rules.canPlayAfter(single, pair));
    }

    @Test
    void playOrderWithSingles() {
        final TichuRules rules = new TichuRules();
        assertTrue(rules.canPlayAfter(newPlay(Pagoda_2), newPlay(Star_7)));
        assertFalse(rules.canPlayAfter(newPlay(Star_3), newPlay(Pagoda_2)));
        assertFalse(
            rules.canPlayAfter(newPlay(Star_2), newPlay(Pagoda_2)),
            "next play must be strictly higher"
        );
    }

    @Test
    void playOrderWithPairs() {
        final TichuRules rules = new TichuRules();
        assertTrue(
            rules.canPlayAfter(
                newPlay(Pagoda_2, Star_2),
                newPlay(Sword_7, Star_7)
            )
        );
        assertFalse(
            rules.canPlayAfter(
                newPlay(Star_3, Sword_3),
                newPlay(Pagoda_2, Star_2)
            )
        );
        assertFalse(
            rules.canPlayAfter(
                newPlay(Star_2, Sword_2),
                newPlay(Pagoda_2, Jade_2)
            ),
            "next pair must be strictly higher"
        );
    }

    @Test
    void invalidPlayIsInvalid() {
        final Play play = newPlay(Sword_2, Pagoda_2, Sword_3);
        assertThat(play).isInstanceOf(InvalidPlay.class);
        assertFalse(new TichuRules().isValid(play));
        assertFalse(new TichuRules().isBomb(play));
    }

    @Test
    void findBasicTricks() {
        assertThat(newPlay(K2)).isInstanceOf(Single.class);
        assertThat(newPlay(K2, B2)).isInstanceOf(Pair.class);
        assertThat(newPlay(K2, B2, R2)).isInstanceOf(Triple.class);
        assertThat(newPlay(K2, B3)).isNotInstanceOf(Pair.class);
        assertThat(newPlay(K2, B2, R6)).isNotInstanceOf(Triple.class);
    }

    @Test
    @Disabled("We don't have a factory yet")
    void findFullHouse() {
        assertThat(newPlay(K2, B2, R2, R3, B3)).isInstanceOf(FullHouse.class);
    }

    @Test
    @Disabled("We don't have a factory yet")
    void consecutivePairs() {
        assertThat(newPlay(K2, B2, R3, B3)).isInstanceOf(
            ConsecutivePairs.class
        );
        assertThat(newPlay(K2, B2, R4, B4)).isNotInstanceOf(
            ConsecutivePairs.class
        );
        assertThat(newPlay(K2, B2, G3, R3, R4, B4)).isInstanceOf(
            ConsecutivePairs.class
        );
    }

    private Play newPlay(Card... cards) {
        return new TichuRules().validate(new HashSet<>(Arrays.asList(cards)));
    }
}
