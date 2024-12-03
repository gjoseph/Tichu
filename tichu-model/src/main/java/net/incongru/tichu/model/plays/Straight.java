package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.Card.CardNumbers.Ace;
import static net.incongru.tichu.model.Card.CardNumbers.Two;
import static net.incongru.tichu.model.Card.CardSpecials.Dog;
import static net.incongru.tichu.model.Card.CardSpecials.Dragon;
import static net.incongru.tichu.model.Card.CardSpecials.Phoenix;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.incongru.tichu.model.Card;

/**
 *
 */
public class Straight extends AbstractPlay<Straight> {

    private final Factory.SubstituteCardValue phoenixSubstitute;
    private final boolean bombyBomb;

    private Straight(
        Set<Card> cards,
        Factory.SubstituteCardValue phoenixSubstitute,
        boolean bombyBomb
    ) {
        super(cards);
        this.phoenixSubstitute = phoenixSubstitute;
        this.bombyBomb = bombyBomb;
    }

    public int size() {
        return getCards().size();
    }

    public Card.CardValue getLowerBound() {
        final Collection<Card.CardValue> values = getCardValuesWithPhoenix();
        return Collections.min(values, Card.Comparators.V_BY_PLAY_ORDER);
    }

    public Card.CardValue getHigherBound() {
        final Collection<Card.CardValue> values = getCardValuesWithPhoenix();
        return Collections.max(values, Card.Comparators.V_BY_PLAY_ORDER);
    }

    private Collection<Card.CardValue> getCardValuesWithPhoenix() {
        final Collection<Card.CardValue> values = Lists.newArrayList(
            Collections2.transform(getCards(), Card::getVal)
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

            final List<Card.CardValue> values = Lists.newArrayList(
                Collections2.transform(cards, Card::getVal)
            );
            values.sort(Card.Comparators.V_BY_PLAY_ORDER);

            // Those are illegal in a street
            if (values.contains(Dog) || values.contains(Dragon)) {
                return null;
            }

            // Keep track of what the Phoenix is substituted for
            boolean phoenixIsAvail = values.remove(Phoenix);
            SubstituteCardValue sub = null;

            // starting at the second card, and compare it to previous... should do, right ?
            for (int i = 1; i < values.size(); i++) {
                final Card.CardValue curr = values.get(i);
                final Card.CardValue prev = values.get(i - 1);

                // Diff between previous and current card should be "1"
                if (curr.playOrder() - prev.playOrder() == 1) {
                    continue;
                }
                // If we haven't "used" the phoenix yet, we can try to fit it in a gap
                if (
                    phoenixIsAvail && curr.playOrder() - prev.playOrder() == 2
                ) {
                    sub = new SubstituteCardValue(prev.playOrder() + 1);
                    phoenixIsAvail = false;
                    continue;
                }
                // If not, we have gaps in our street
                return null;
            }

            // If phoenix hasn't been used, we use it for the highest possible position
            if (phoenixIsAvail) {
                final Card.CardValue last = values.get(values.size() - 1);
                final Card.CardValue first = values.get(0);
                if (last == Ace && first.playOrder() > Two.playOrder()) {
                    // then we use it "before" the first card
                    sub = new SubstituteCardValue(first.playOrder() - 1);
                } else if (last.playOrder() < Ace.playOrder()) {
                    sub = new SubstituteCardValue(last.playOrder() + 1);
                } else {
                    // we have an unusable phoenix
                    return null;
                }
            }

            final Card.CardSuit cardSuitTest = cards
                .iterator()
                .next()
                .getSuit();
            final boolean isBomb = cards
                .stream()
                .allMatch(card -> card.getSuit() == cardSuitTest);

            return new Straight(cards, sub, isBomb);
        }

        private static class SubstituteCardValue implements Card.CardValue {

            private final Card.CardValue substitutedValue;
            private final int subPlayOrder;

            public SubstituteCardValue(int playOrder) {
                this.subPlayOrder = playOrder;
                this.substitutedValue = Card.CardNumbers.byPlayOrder(playOrder);
            }

            @Override
            public boolean isSpecial() {
                return true;
            }

            @Override
            public int scoreValue() {
                return Phoenix.scoreValue();
            }

            @Override
            public int playOrder() {
                return subPlayOrder;
            }

            @Override
            public String niceName() {
                return substitutedValue.niceName();
            }

            @Override
            public char shortName() {
                return (char) ('0' + subPlayOrder);
            }
        }
    }
}
