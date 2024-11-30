package net.incongru.tichu.model;

import static net.incongru.tichu.model.Card.Comparators.BY_SUIT;
import static net.incongru.tichu.model.util.DeckConstants.Dog;
import static net.incongru.tichu.model.util.DeckConstants.Dragon;
import static net.incongru.tichu.model.util.DeckConstants.Jade_Queen;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static net.incongru.tichu.model.util.DeckConstants.Phoenix;
import static net.incongru.tichu.model.util.DeckConstants.Sword_7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.FluentIterable;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.Test;

/**
 *
 */
class CardTest {

    @Test
    void cardNamesAreOkay() {
        assertEquals("Ace of Sword", new Card(Card.CardNumbers.Ace, Card.CardSuit.Sword).name());
        assertEquals("Queen of Star", new Card(Card.CardNumbers.Queen, Card.CardSuit.Star).name());
        assertEquals("7 of Pagoda", new Card(Card.CardNumbers.Seven, Card.CardSuit.Pagoda).name());
        assertEquals("Dragon", new Card(Card.CardSpecials.Dragon, null).name());
    }

    @Test
    void suitComparatorPutsDogBeforeNumberedCards() {
        assertThat(BY_SUIT.compare(Dog, DeckConstants.B2)).isLessThan(0);
    }

    @Test
    void suitComparatorDoesNotMessUpSpecialCards() {
        final Card[] array = { Dog, Dragon, MahJong, Phoenix };
        // We don't really care about the order that much, we just want to make sure the comparator is consistent with equals, i.e does not equal two cards which are not the same
        assertThat(
            FluentIterable.from(array).toSortedList(BY_SUIT)
        ).containsOnly(array);
        assertThat(
            FluentIterable.from(array).toSortedSet(BY_SUIT)
        ).containsOnly(array);
        assertThat(
            FluentIterable.from(new CardDeck().allRemaining()).toSortedList(
                BY_SUIT
            )
        ).hasSize(56);
        assertThat(
            FluentIterable.from(new CardDeck().allRemaining()).toSortedSet(
                BY_SUIT
            )
        ).hasSize(56);
    }

    @Test
    void predicates() {
        assertThat(Card.Predicates.is(Card.CardSpecials.Dog)).accepts(Dog);
        assertThat(Card.Predicates.is(Card.CardSpecials.Dog)).rejects(
            Jade_Queen,
            MahJong,
            Dragon,
            Phoenix
        );

        assertThat(
            Card.Predicates.is(Card.CardNumbers.Queen, Card.CardSuit.Jade)
        ).accepts(Jade_Queen);
        assertThat(
            Card.Predicates.is(Card.CardNumbers.Queen, Card.CardSuit.Jade)
        ).rejects(Sword_7, Dog, MahJong, Dragon, Phoenix);

        assertThat(Card.Predicates.is(null)).rejects(Dog, Jade_Queen);
        assertThat(Card.Predicates.is(null, null)).rejects(Dog, Jade_Queen);
    }
}
