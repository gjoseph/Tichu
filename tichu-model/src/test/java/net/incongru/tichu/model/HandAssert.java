package net.incongru.tichu.model;

import net.incongru.tichu.model.card.Card;
import org.assertj.core.api.AbstractAssert;

public class HandAssert extends AbstractAssert<HandAssert, Player.Hand> {

    public static HandAssert assertThat(Player.Hand actual) {
        return new HandAssert(actual);
    }

    public HandAssert(Player.Hand actual) {
        super(actual, HandAssert.class);
    }

    public HandAssert containsOnly(Card... cards) {
        return containsOnly(Set.of(cards));
    }

    public HandAssert containsOnly(Set<Card> cards) {
        contains(cards);
        if (cards.size() != actual.size()) {
            failWithMessage(
                "Expected cards to be <%s> but was <%s>",
                cards,
                actual.toDebugString()
            );
        }
        return this;
    }

    public HandAssert contains(Card... cards) {
        return contains(Set.of(cards));
    }

    public HandAssert contains(Set<Card> cards) {
        isNotNull();
        if (!actual.hasAll(cards)) {
            failWithMessage(
                "Expected cards to contain <%s> but was <%s>",
                cards,
                actual.toDebugString()
            );
        }
        return this;
    }
}
