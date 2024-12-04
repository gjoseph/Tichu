package net.incongru.tichu.model.card;

import com.google.common.base.Preconditions;
import java.util.Objects;

record CardImpl(CardValue val, CardSuit suit) implements Card {
    CardImpl {
        Objects.requireNonNull(val, "Card value can't be null");
        if (val.isSpecial()) {
            Preconditions.checkState(
                suit == null,
                "Special cards don't have a suit"
            );
        } else {
            Objects.requireNonNull(suit, "Suit can't be null for " + val);
        }
    }

    public String name() {
        return val.niceName() + (val.isSpecial() ? "" : " of " + suit);
    }

    public String shortName() {
        return (
            String.valueOf(val.isSpecial() ? '*' : suit.shortName()) +
            val.shortName()
        );
    }
}
