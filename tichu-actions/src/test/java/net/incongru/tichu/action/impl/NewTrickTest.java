package net.incongru.tichu.action.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static net.incongru.tichu.action.ActionResultAssert.assertThat;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.B3;
import static net.incongru.tichu.model.util.DeckConstants.B4;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NewTrickTest {

    private TestGameContext ctx;

    @BeforeEach
    void setUp() {
        ctx = new TestGameContext().initialised().withSamplePlayers();
    }

    @Test
    void failsIfTrickInProgress() {
        ctx.withCards(Set.of(B2), Set.of(B3), Set.of(B4), Set.of(MahJong))
                .allReady();
        // we still have cards to play
        assertThrows(IllegalStateException.class, () -> new NewTrick().exec(ctx));
    }

    @Test
    void successWhenAllCardsPlayed() {
        ctx.withCards(Set.of(MahJong), Set.of(B3), Set.of(B2), Set.of(B4))
                .allReady();
        // play all the cards
        assertThat(new PlayerPlays("alex", Set.of(MahJong)).exec(ctx)).isSuccessResult();
        assertThat(new PlayerPlays("jules", Set.of(B2)).exec(ctx)).isSuccessResult();
        assertThat(new PlayerPlays("charlie", Set.of(B3)).exec(ctx)).isSuccessResult();
        assertThat(new PlayerPlays("quinn", Set.of(B4)).exec(ctx)).isSuccessResult();
        // all players now explicitly pass
        assertThat(new PlayerPlays("alex", Set.of()).exec(ctx)).isSuccessResult();
        assertThat(new PlayerPlays("jules", Set.of()).exec(ctx)).isSuccessResult();
        assertThat(new PlayerPlays("charlie", Set.of()).exec(ctx)).isSuccessResult();

        assertThat(new NewTrick().exec(ctx)).isSuccessResult();
    }
}