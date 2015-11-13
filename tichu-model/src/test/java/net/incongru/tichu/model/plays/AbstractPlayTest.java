package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.DeckConstants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.CardDeck;

public class AbstractPlayTest {
    @Test
    public void collectionsDotMinWithComparatorByPlayOrderToFindLowestCard() {
        CardDeck d = new CardDeck();
        final List<Card> cards = Arrays.asList(Pagoda_9, Pagoda_7, Sword_4, Pagoda_5, Pagoda_6, Pagoda_4, Pagoda_8, Pagoda_10);
        final Card min = Collections.min(cards, Card.Comparators.BY_PLAY_ORDER);
        assertThat(min, Matchers.either(equalTo(Pagoda_4)).or(equalTo(Sword_4)));

        // Just checking my assumptions about matchers below:
        assertThat(cards, containsInAnyOrder(
                equalTo(Pagoda_4),
                equalTo(Pagoda_5),
                equalTo(Pagoda_6),
                equalTo(Pagoda_7),
                equalTo(Pagoda_8),
                equalTo(Pagoda_9),
                equalTo(Pagoda_10),
                equalTo(Sword_4)
        ));
    }


}
