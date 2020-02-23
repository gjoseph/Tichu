package net.incongru.tichu.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.nullsFirst;

/**
 *
 */
public class Card {
    private final CardValue val;
    private final CardSuit suit;

    public Card(CardValue val, CardSuit suit) {
        Objects.requireNonNull(val, "Card value can't be null");
        if (val.isSpecial()) {
            throw new IllegalArgumentException("Special cards don't have a suit");
        }
        Objects.requireNonNull(suit, "Suit can't be null");
        this.val = val;
        this.suit = suit;
    }

    public Card(CardValue val) {
        Objects.requireNonNull(val, "Card value can't be null");
        if (!val.isSpecial()) {
            throw new IllegalArgumentException("Numbered/Figure cards require a suit");
        }
        this.val = val;
        this.suit = null;
    }

    public CardValue getVal() {
        return val;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public String name() {
        return val.niceName() + (val.isSpecial() ? "" : " of " + suit);
    }

    public String shortName() {
        return String.valueOf(val.isSpecial() ? '*' : suit.shortName()) + val.shortName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(val, card.val) &&
                Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, suit);
    }

    @Override
    public String toString() {
        return shortName();
    }

    public enum CardSuit {
        // K is BlacK (Blue exists), which conflicts with King :/
        // shortNames are the initials of colors rather than the actual name of the suit
        Jade('G'), Sword('K'), Pagoda('B'), Star('R');
        final char shortName;

        CardSuit(char shortName) {
            this.shortName = shortName;
        }

        public char shortName() {
            return shortName;
        }
    }

    public interface CardValue {
        boolean isSpecial();

        int scoreValue();

        /**
         * @return the order or "position" in which this card can be played (e.g a card can be played only if the previously played card has a lower playOrder), starting at 1 index.
         * Both the {@link CardSpecials#MahJong} and {@link CardSpecials#Dog} thus return 1 for this. {@link CardSpecials#Phoenix} returns -1 because it is dependent on the previously played card (prev+0.5).
         */
        int playOrder();

        String niceName();

        char shortName();
    }

    public enum CardNumbers implements CardValue {
        Two(0, 2),
        Three(0, 3),
        Four(0, 4),
        Five(5, 5),
        Six(0, 6),
        Seven(0, 7),
        Eight(0, 8),
        Nine(0, 9),
        Ten(10, 10),
        Jack(0, 11),
        Queen(0, 12),
        King(10, 13),
        Ace(0, 14);

        public static CardNumbers byPlayOrder(int playOrder) {
            return Arrays.stream(values()).filter(c -> c.playOrder() == playOrder).findFirst().get();
        }

        final int scoreValue;
        final int playOrder;

        CardNumbers(int scoreValue, int playOrder) {
            this.scoreValue = scoreValue;
            this.playOrder = playOrder;
        }

        @Override
        public boolean isSpecial() {
            return false;
        }

        @Override
        public int scoreValue() {
            return this.scoreValue;
        }

        @Override
        public int playOrder() {
            return playOrder;
        }

        /**
         * Yes, I capitalized the names/values of the enums above just so I wouldn't have to code or import a capitalize() method for the Card.name() method.
         */
        @Override
        public String niceName() {
            return (playOrder <= 10 ? String.valueOf(playOrder) : name());
        }

        @Override
        public char shortName() {
            return (playOrder < 10 ? (char) (playOrder + '0') : playOrder == 10 ? '0' : name().charAt(0));
        }
    }

    // I wish enums could extend AbstractCardValue but they can't, hence the copy paste below.
    public enum CardSpecials implements CardValue {

        MahJong(0, 1, '1'),
        Dog(0, 1, 'H'), // Hund in German, D is for Dragon
        Phoenix(-25, -1, 'P'),
        Dragon(25, 50, 'D');

        final int scoreValue;
        final int playOrder;
        final char shortName;

        CardSpecials(int scoreValue, int playOrder, char shortName) {
            this.scoreValue = scoreValue;
            this.playOrder = playOrder;
            this.shortName = shortName;
        }

        @Override
        public boolean isSpecial() {
            return true;
        }

        @Override
        public int scoreValue() {
            return this.scoreValue;
        }

        @Override
        public int playOrder() {
            return playOrder;
        }

        /**
         * Yes, I capitalized the names/values of the enums above just so I wouldn't have to code or import a capitalize() method for the Card.name() method.
         */
        @Override
        public String niceName() {
            return name();
        }

        @Override
        public char shortName() {
            return shortName;
        }

    }

    /**
     * These comparators are probably consistent with equals()...
     */
    public static class Comparators {
        private static final Comparator<CardValue> V_PLAY_ORDER = comparingInt(CardValue::playOrder).thenComparing(CardValue::shortName);
        private static final Comparator<Card> PLAY_ORDER = comparing(Card::getVal, V_PLAY_ORDER);
        private static final Comparator<Card> SUIT = comparing(Card::getSuit, nullsFirst(comparing(CardSuit::shortName)));

        public static final Comparator<Card> BY_PLAY_ORDER = PLAY_ORDER.thenComparing(SUIT);
        public static final Comparator<Card> BY_SUIT = SUIT.thenComparing(PLAY_ORDER);
        public static final Comparator<CardValue> V_BY_PLAY_ORDER = V_PLAY_ORDER;
    }


}
