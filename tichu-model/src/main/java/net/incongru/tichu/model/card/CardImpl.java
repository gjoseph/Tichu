package net.incongru.tichu.model.card;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.Nullable;

record CardImpl(CardValue val, @Nullable CardSuit suit) implements Card {
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

    // "suit is never null if val.isSpecial is false"
    // @EnsuresNonNullIf(expression="val.isSpecial()", result=true)
    public String shortName() {
        return (
            String.valueOf(val.isSpecial() ? '*' : suit.shortName()) +
            val.shortName()
        );
    }
}
