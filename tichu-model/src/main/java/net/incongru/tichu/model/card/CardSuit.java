package net.incongru.tichu.model.card;

public enum CardSuit {
    Jade('G'), // Green
    Pagoda('B'), // Blue
    Sword('K'), // blacK,
    Star('R'); // Red

    private final char shortName;

    CardSuit(char shortName) {
        this.shortName = shortName;
    }

    public char shortName() {
        return shortName;
    }
}
