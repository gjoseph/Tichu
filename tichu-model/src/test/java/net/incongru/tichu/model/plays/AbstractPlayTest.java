package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.util.DeckConstants.Pagoda_10;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_4;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_5;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_6;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_7;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_8;
import static net.incongru.tichu.model.util.DeckConstants.Pagoda_9;
import static net.incongru.tichu.model.util.DeckConstants.Sword_4;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import net.incongru.tichu.model.card.Card;
import org.junit.jupiter.api.Test;

class AbstractPlayTest {

    @Test
    void correctlyFindLowestCard() {
        final Set<Card> cards = Set.of(
            Pagoda_9,
            Pagoda_7,
            Sword_4,
            Pagoda_5,
            Pagoda_6,
            Pagoda_4,
            Pagoda_8,
            Pagoda_10
        );
        final FakePlay play = new FakePlay(cards);
        final Card smallestCard = play.smallestCard();
        assertThat(smallestCard).isIn(Pagoda_4, Sword_4); // we don't really care which of these 2 it is

        // Just checking my assumptions about matchers below:
        assertThat(cards).contains(
            Pagoda_4,
            Pagoda_5,
            Pagoda_6,
            Pagoda_7,
            Pagoda_8,
            Pagoda_9,
            Pagoda_10,
            Sword_4
        );
    }

    private static class FakePlay extends AbstractPlay<FakePlay> {

        private FakePlay(Set<Card> cards) {
            super(cards);
        }

        @Override
        protected boolean canBePlayedAfterTypeSafe(FakePlay other) {
            throw new IllegalStateException();
        }

        @Override
        public String describe() {
            throw new IllegalStateException();
        }
    }
}
