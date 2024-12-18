package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.card.CardNumbers.Ace;
import static net.incongru.tichu.model.card.CardNumbers.Two;
import static net.incongru.tichu.model.card.CardSpecials.Dog;
import static net.incongru.tichu.model.card.CardSpecials.Dragon;
import static net.incongru.tichu.model.card.CardSpecials.Phoenix;
import static net.incongru.tichu.model.card.SubstituteCardValue.substituteFor;

import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardComparators;
import net.incongru.tichu.model.card.CardSuit;
import net.incongru.tichu.model.card.CardValue;

/**
 *
 */
public class Straight extends AbstractPlay<Straight> {

    private final CardValue phoenixSubstitute;
    private final boolean bombyBomb;

    private Straight(
        Set<Card> cards,
        CardValue phoenixSubstitute,
        boolean bombyBomb
    ) {
        super(cards);
        this.phoenixSubstitute = phoenixSubstitute;
        this.bombyBomb = bombyBomb;
    }

    public int size() {
        return getCards().size();
    }

    public CardValue getLowerBound() {
        final Collection<CardValue> values = getCardValuesWithPhoenix();
        return Collections.min(values, CardComparators.V_BY_PLAY_ORDER);
    }

    public CardValue getHigherBound() {
        final Collection<CardValue> values = getCardValuesWithPhoenix();
        return Collections.max(values, CardComparators.V_BY_PLAY_ORDER);
    }

    private Collection<CardValue> getCardValuesWithPhoenix() {
        final Collection<CardValue> values = new ArrayList<>(
            Collections2.transform(getCards(), Card::val)
        );
        values.removeIf(v -> v == Phoenix);
        if (phoenixSubstitute != null) {
            values.add(phoenixSubstitute);
        }
        return values;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(Straight other) {
        return (
            other.size() == this.size() &&
            this.getLowerBound().playOrder() > other.getLowerBound().playOrder()
        );
    }

    @Override
    public String describe() {
        final StringBuilder s = new StringBuilder();
        s
            .append(name())
            .append(" of ")
            .append(size())
            .append(", from ")
            .append(getLowerBound().niceName())
            .append(" to ")
            .append(getHigherBound().niceName());
        if (phoenixSubstitute != null) {
            s
                .append(" with a ")
                .append(Phoenix.niceName())
                .append(" substituting for the ")
                .append(phoenixSubstitute.niceName());
        }
        return s.toString();
    }

    @Override
    public boolean isBomb() {
        return bombyBomb;
    }

    public static class Factory implements PlayFactory<Straight> {

        @Override
        public Straight is(Set<Card> cards) {
            if (cards.size() < 5) {
                return null;
            }

            final List<CardValue> values = new ArrayList<>(
                Collections2.transform(cards, Card::val)
            );
            values.sort(CardComparators.V_BY_PLAY_ORDER);

            // Those are illegal in a street
            if (values.contains(Dog) || values.contains(Dragon)) {
                return null;
            }

            // Keep track of what the Phoenix is substituted for
            boolean phoenixIsAvail = values.remove(Phoenix);
            CardValue sub = null;

            // starting at the second card, and compare it to previous... should do, right ?
            for (int i = 1; i < values.size(); i++) {
                final CardValue curr = values.get(i);
                final CardValue prev = values.get(i - 1);

                // Diff between previous and current card should be "1"
                if (curr.playOrder() - prev.playOrder() == 1) {
                    continue;
                }
                // If we haven't "used" the phoenix yet, we can try to fit it in a gap
                if (
                    phoenixIsAvail && curr.playOrder() - prev.playOrder() == 2
                ) {
                    sub = substituteFor(prev.playOrder() + 1);
                    phoenixIsAvail = false;
                    continue;
                }
                // If not, we have gaps in our street
                return null;
            }

            // If phoenix hasn't been used, we use it for the highest possible position
            if (phoenixIsAvail) {
                final CardValue last = values.getLast();
                final CardValue first = values.getFirst();
                if (last == Ace && first.playOrder() > Two.playOrder()) {
                    // then we use it "before" the first card
                    sub = substituteFor(first.playOrder() - 1);
                } else if (last.playOrder() < Ace.playOrder()) {
                    sub = substituteFor(last.playOrder() + 1);
                } else {
                    // we have an unusable phoenix
                    return null;
                }
            }

            Card card1 = cards.iterator().next();
            final CardSuit cardSuitTest = card1.suit();
            final boolean isBomb = cards
                .stream()
                .allMatch(card -> card.suit() == cardSuitTest);

            return new Straight(cards, sub, isBomb);
        }
    }
}
