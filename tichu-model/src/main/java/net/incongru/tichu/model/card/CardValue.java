package net.incongru.tichu.model.card;

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
