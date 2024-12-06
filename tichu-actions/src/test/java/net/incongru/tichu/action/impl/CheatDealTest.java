package net.incongru.tichu.action.impl;

import static net.incongru.tichu.action.ActionResultAssert.assertThat;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.B3;
import static net.incongru.tichu.model.util.DeckConstants.G2;
import static net.incongru.tichu.model.util.DeckConstants.G5;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.collect.Sets;
import java.util.Set;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.context.GameContext;
import net.incongru.tichu.model.HandAssert;
import net.incongru.tichu.model.UserId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CheatDealTest {

    private TestGameContext ctx;

    @BeforeEach
    void setUp() {
        ctx = new TestGameContext().initialised().withSamplePlayers();
    }

    @Test
    void failsInNormalGame() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame()
            .exec(ctx, InitialiseGameParam.withActor(UserId.of("alex")));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("alex"), 0));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("charlie"), 0));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("jules"), 1));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("quinn"), 1));

        Assertions.assertThatThrownBy(() -> {
            new CheatDeal()
                .exec(
                    ctx,
                    CheatDealParam.withActor(
                        UserId.of("jules"),
                        Sets.newHashSet(MahJong, B2)
                    )
                );
        })
            .isInstanceOf(IllegalStateException.class)
            .hasMessageMatching("(?i).*can't cheat.*");
    }

    @Test
    void worksInSimulatedGame() {
        final GameContext ctx = new TestGameContext();
        new InitialiseSimulatedGame()
            .exec(ctx, InitialiseGameParam.withActor(UserId.of("alex")));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("alex"), 0));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("charlie"), 0));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("jules"), 1));
        new JoinTable()
            .exec(ctx, JoinTableParam.withActor(UserId.of("quinn"), 1));

        Assertions.assertThatCode(() -> {
            new CheatDeal()
                .exec(
                    ctx,
                    CheatDealParam.withActor(
                        UserId.of("jules"),
                        Sets.newHashSet(MahJong, B2)
                    )
                );
        }).doesNotThrowAnyException();
    }

    @Test
    void distributesCardsAsSpecified() {
        final CheatDeal action = new CheatDeal();

        assertThat(
            action.exec(
                ctx,
                CheatDealParam.withActor(
                    UserId.of("alex"),
                    Set.of(MahJong, G2, G5)
                )
            )
        ).isSuccessResult();
        HandAssert.assertThat(
            ctx.player(UserId.of("alex")).hand()
        ).containsOnly(MahJong, G2, G5);
    }

    @Test
    void tooLateIfGameReady() {
        // pre-deal some cards
        new CheatDeal()
            .exec(
                ctx,
                CheatDealParam.withActor(
                    UserId.of("charlie"),
                    Set.of(MahJong, B2, B3)
                )
            );
        ctx.allReady();

        //
        assertThrows(IllegalStateException.class, () -> {
            new CheatDeal()
                .exec(
                    ctx,
                    CheatDealParam.withActor(UserId.of("alex"), Set.of(G2, G5))
                );
        });
    }

    @Test
    @Disabled(
        "we currently can't check for this because PlayerIsReady action auto-starts the game... which is maybe ok. Delete this test if we're happy with that"
    )
    void tooLateIfGameStarted() {
        fail();
    }
}
