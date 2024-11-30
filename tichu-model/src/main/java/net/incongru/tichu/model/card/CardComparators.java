package net.incongru.tichu.model.card;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.nullsFirst;

/**
 * These comparators are probably consistent with equals()...
 */
public class CardComparators {
    private static final Comparator<CardValue> V_PLAY_ORDER = comparingInt(CardValue::playOrder).thenComparing(CardValue::shortName);
    private static final Comparator<Card> PLAY_ORDER = comparing(Card::val, V_PLAY_ORDER);
    private static final Comparator<Card> SUIT = comparing(Card::suit, nullsFirst(comparing(CardSuit::shortName)));

    public static final Comparator<Card> BY_PLAY_ORDER = PLAY_ORDER.thenComparing(SUIT);
    public static final Comparator<Card> BY_SUIT = SUIT.thenComparing(PLAY_ORDER);
    public static final Comparator<CardValue> V_BY_PLAY_ORDER = V_PLAY_ORDER;
}
