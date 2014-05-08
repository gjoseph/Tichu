package net.incongru.tichu.model;

import lombok.Value;

/**
 * // TODO constructor currently permits to construct Dragon of Jade. Do we care ? CardDeck takes care of this.
 */
@Value
class Card {
    CardValue val;
    CardSuit suit;

    String name() {
        return val.niceName() + (val.isSpecial() ? "" : " of " + suit);
    }

    enum CardSuit {
        Jade, Sword, Pagoda, Star
    }

    enum CardNumbers implements CardValue {
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
        Kind(10, 13),
        Ace(0, 14);

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
    }

    // I wish enums could extend AbstractCardValue but they can't, hence the copy paste below.
    enum CardSpecials implements CardValue {
        One(0, 1),
        Dog(0, 1),
        Phoenix(-25, -1),
        Dragon(25, 50);

        final int scoreValue;
        final int playOrder;

        CardSpecials(int scoreValue, int playOrder) {
            this.scoreValue = scoreValue;
            this.playOrder = playOrder;
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

    }

    interface CardValue {
        boolean isSpecial();

        int scoreValue();

        /**
         * TODO : order is maybe not the right word
         *
         * @return the "position" in which this card can be played (e.g a card can be played only if the previously played card has a lower playOrder), starting at 1 index.
         *         Both the {@link CardSpecials#One} and {@link CardSpecials#Dog} thus return 1 for this. {@link CardSpecials#Phoenix} returns -1 because it is dependent on the previously played card (prev+0.5).
         */
        int playOrder();

        String niceName();
    }
}
