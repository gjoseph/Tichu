package net.incongru.tichu.model.plays;

import com.google.common.collect.Collections2;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Abstract implementation of {@link Play} with N cards of the same value.\
 *
 * @see Pair
 * @see Triple
 * @see BombOf4
 */
public abstract class NSameValue<P extends NSameValue> extends AbstractPlay<P> {
    protected final Card.CardValue value;

    public NSameValue(Set<Card> cards, Card.CardValue value) {
        super(cards);
        this.value = value;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(P other) {
        return other.getCards().size() == this.getCards().size() // Not very useful, since we check the class, but at least explicit
                && other.value.playOrder() < this.value.playOrder();
    }

    @Override
    public String describe() {
        return name() + " of " + value + "s";
    }

    public abstract static class NSameValuesFactory<P extends Play<P>> implements PlayFactory<P> {

        @Override
        public P is(Set<Card> cards) {
            if (cards.size() != expectedSize()) {
                return null;
            }

            final Collection<Card.CardValue> values = Collections2.transform(cards, Card::getVal);
            final Stream<Card.CardValue> distinct = values.stream().distinct();
            if (distinct.count() != 1) {
                return null;
            }

            final Card.CardValue value = values.stream().findFirst().get();
            return newPlay(cards, value);
        }

        protected abstract int expectedSize();

        protected abstract P newPlay(Set<Card> cards, Card.CardValue value);
    }
}
