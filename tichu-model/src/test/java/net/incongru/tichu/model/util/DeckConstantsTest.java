package net.incongru.tichu.model.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeckConstantsTest {

    @Test
    public void cardNamesAreCaseInsensitive() {
        assertThat(DeckConstants.byName("B2")).isEqualTo(DeckConstants.Pagoda_2);
        assertThat(DeckConstants.byName("b2")).isEqualTo(DeckConstants.Pagoda_2);
        assertThat(DeckConstants.byName("paGODA_2")).isEqualTo(DeckConstants.Pagoda_2);
        assertThat(DeckConstants.byName("mahjonG")).isEqualTo(DeckConstants.MahJong);
        assertThat(DeckConstants.byName("_d")).isEqualTo(DeckConstants.Dragon);
    }

    @Test
    public void specialCards() {
        assertThat(DeckConstants.byName("_1")).isEqualTo(DeckConstants.MahJong);
        assertThat(DeckConstants.byName("MahJong")).isEqualTo(DeckConstants.MahJong);

        assertThat(DeckConstants.byName("_D")).isEqualTo(DeckConstants.Dragon);
        assertThat(DeckConstants.byName("Dragon")).isEqualTo(DeckConstants.Dragon);

        assertThat(DeckConstants.byName("Dog")).isEqualTo(DeckConstants.Dog);
        assertThat(DeckConstants.byName("_H")).isEqualTo(DeckConstants.Dog);

        assertThat(DeckConstants.byName("Phoenix")).isEqualTo(DeckConstants.Phoenix);
        assertThat(DeckConstants.byName("_P")).isEqualTo(DeckConstants.Phoenix);
    }
}