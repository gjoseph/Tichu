package net.incongru.tichu.model.card;

import static net.incongru.tichu.model.card.CardComparators.BY_SUIT;
import static net.incongru.tichu.model.util.DeckConstants.Dog;
import static net.incongru.tichu.model.util.DeckConstants.Dragon;
import static net.incongru.tichu.model.util.DeckConstants.Jade_Queen;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static net.incongru.tichu.model.util.DeckConstants.Phoenix;
import static net.incongru.tichu.model.util.DeckConstants.Sword_7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.FluentIterable;
import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.util.DeckConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 */
class CardTest {

    @Test
    void cardNamesAreOkay() {
        assertEquals(
            "Ace of Sword",
            Card.of(CardNumbers.Ace, CardSuit.Sword).name()
        );
        assertEquals(
            "Queen of Star",
            Card.of(CardNumbers.Queen, CardSuit.Star).name()
        );
        assertEquals(
            "7 of Pagoda",
            Card.of(CardNumbers.Seven, CardSuit.Pagoda).name()
        );
        assertEquals("Dragon", Card.of(CardSpecials.Dragon).name());
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
        Assertions.assertThat(
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
        assertThat(CardPredicates.is(CardSpecials.Dog)).accepts(Dog);
        assertThat(CardPredicates.is(CardSpecials.Dog)).rejects(
            Jade_Queen,
            MahJong,
            Dragon,
            Phoenix
        );

        assertThat(CardPredicates.is(CardNumbers.Queen, CardSuit.Jade)).accepts(
            Jade_Queen
        );
        assertThat(CardPredicates.is(CardNumbers.Queen, CardSuit.Jade)).rejects(
            Sword_7,
            Dog,
            MahJong,
            Dragon,
            Phoenix
        );

        assertThat(CardPredicates.is(null)).rejects(Dog, Jade_Queen);
        assertThat(CardPredicates.is(null, null)).rejects(Dog, Jade_Queen);
    }
}
