package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.impl.NewTrick.NewTrickParam;
import net.incongru.tichu.action.impl.PlayerPlays.PlayerPlaysParam;
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
        assertThrows(IllegalStateException.class, () -> new NewTrick().exec(ctx, new NewTrickParam()));
    }

    @Test
    void successWhenAllCardsPlayed() {
        ctx.withCards(Set.of(MahJong), Set.of(B3), Set.of(B2), Set.of(B4))
                .allReady();
        // play all the cards
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("alex", Set.of(MahJong)))).isSuccessResult();
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("jules", Set.of(B2)))).isSuccessResult();
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("charlie", Set.of(B3)))).isSuccessResult();
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("quinn", Set.of(B4)))).isSuccessResult();
        // all players now explicitly pass
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("alex", Set.of()))).isSuccessResult();
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("jules", Set.of()))).isSuccessResult();
        assertThat(new PlayerPlays().exec(ctx, new PlayerPlaysParam("charlie", Set.of()))).isSuccessResult();

        assertThat(new NewTrick().exec(ctx, new NewTrickParam())).isSuccessResult();
    }
}