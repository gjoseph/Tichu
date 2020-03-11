package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.impl.CheatDeal.CheatDealParam;
import net.incongru.tichu.model.HandAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static net.incongru.tichu.action.ActionResultAssert.assertThat;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.B3;
import static net.incongru.tichu.model.util.DeckConstants.G2;
import static net.incongru.tichu.model.util.DeckConstants.G5;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class CheatDealTest {

    private TestGameContext ctx;

    @BeforeEach
    void setUp() {
        ctx = new TestGameContext().initialised().withSamplePlayers();
    }

    @Test
    void distributesCardsAsSpecified() {
        final CheatDeal action = new CheatDeal();

        assertThat(action.exec(ctx, new CheatDealParam("alex", Set.of(MahJong, G2, G5)))).isSuccessResult();
        HandAssert.assertThat(ctx.player("alex").hand()).containsOnly(MahJong, G2, G5);
    }

    @Test
    void tooLateIfGameReady() {
        // pre-deal some cards
        new CheatDeal().exec(ctx, new CheatDealParam("charlie", Set.of(MahJong, B2, B3)));
        ctx.allReady();

        //
        assertThrows(IllegalStateException.class, () -> {
            new CheatDeal().exec(ctx, new CheatDealParam("alex", Set.of(G2, G5)));
        });
    }

    @Test
    @Disabled("we currently can't check for this because PlayerIsReady action auto-starts the game... which is maybe ok. Delete this test if we're happy with that")
    void tooLateIfGameStarted() {
        fail();
    }

}