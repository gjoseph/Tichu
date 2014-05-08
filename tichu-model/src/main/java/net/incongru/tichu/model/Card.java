package net.incongru.tichu.model;

import lombok.Value;

/**
 * @author gjoseph
 */
@Value
class Card {
    CardValue val;
    CardSuit suit;

    String name() {
        return val + (val.isSpecial() ? "" : " of " + suit);
    }

    enum CardSuit {
        Jade, Sword, Pagoda, Star
    }

    enum CardNumbers implements CardValue {
        two(0, 2),
        three(0, 3),
        four(0, 4),
        five(5, 5),
        six(0, 6),
        seven(0, 7),
        height(0, 8),
        nine(0, 9),
        ten(10, 10),
        jack(0, 11),
        queen(0, 12),
        kind(10, 13),
        ace(0, 14);

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
    }

    // I wish enums could extend AbstractCardValue but they can't, hence the copy paste below.
    enum CardSpecials implements CardValue {
        one(0, 1),
        dog(0, 1),
        phoenix(-25, -1),
        dragon(25, 50);

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

    }

    interface CardValue {
        boolean isSpecial();

        int scoreValue();

        /**
         * TODO : order is maybe not the right word
         *
         * @return the "position" in which this card can be played (e.g a card can be played only if the previously played card has a lower playOrder), starting at 1 index.
         *         Both the {@link CardSpecials#one} and {@link CardSpecials#dog} thus return 1 for this. {@link CardSpecials#phoenix} returns -1 because it is dependent on the previously played card (prev+0.5).
         */
        int playOrder();
    }
}
