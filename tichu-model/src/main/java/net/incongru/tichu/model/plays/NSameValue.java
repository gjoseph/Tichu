package net.incongru.tichu.model.plays;

import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardValue;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Abstract implementation of {@link Play} with N cards of the same value.\
 *
 * @see Pair
 * @see Triple
 * @see BombOf4
 */
public abstract class NSameValue<P extends NSameValue> extends AbstractPlay<P> {

    protected final CardValue value;

    protected NSameValue(Set<Card> cards, CardValue value) {
        super(cards);
        this.value = value;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(P other) {
        return (
            other.getCards().size() == this.getCards().size() && // Not very useful, since we check the class, but at least explicit
            other.value.playOrder() < this.value.playOrder()
        );
    }

    @Override
    public String describe() {
        return name() + " of " + value + "s";
    }

    public abstract static class NSameValuesFactory<P extends Play<P>>
        implements PlayFactory<P> {

        @Override
        public @Nullable P is(Set<Card> cards) {
            if (cards.size() != expectedSize()) {
                return null;
            }

            final Collection<CardValue> values = Collections2.transform(
                cards,
                Card::val
            );
            final Stream<CardValue> distinct = values.stream().distinct();
            if (distinct.count() != 1) {
                return null;
            }

            final CardValue value = values.stream().findFirst().get();
            return newPlay(cards, value);
        }

        protected abstract int expectedSize();

        protected abstract P newPlay(Set<Card> cards, CardValue value);
    }
}
