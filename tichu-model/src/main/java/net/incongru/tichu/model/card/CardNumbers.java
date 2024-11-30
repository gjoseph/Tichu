package net.incongru.tichu.model.card;

import java.util.Arrays;

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

    private final int scoreValue;
    private final int playOrder;

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
