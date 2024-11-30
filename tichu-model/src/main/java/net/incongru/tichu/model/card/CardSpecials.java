package net.incongru.tichu.model.card;

public enum CardSpecials implements CardValue {
    MahJong(0, 1, '1'),
    Dog(0, 1, 'H'), // Hund in German, D is for Dragon
    Phoenix(-25, -1, 'P'),
    Dragon(25, 50, 'D');

    private final int scoreValue;
    private final int playOrder;
    private final char shortName;

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
