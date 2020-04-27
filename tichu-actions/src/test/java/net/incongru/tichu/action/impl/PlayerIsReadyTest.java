package net.incongru.tichu.action.impl;

import com.google.common.collect.Sets;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.model.UserId;
import org.junit.jupiter.api.Test;

import static net.incongru.tichu.model.GameAssert.Conditions.notReadyNorStarted;
import static net.incongru.tichu.model.GameAssert.Conditions.started;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlayerIsReadyTest {
    @Test
    void failsWithUnknownPlayer() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, InitialiseGameParam.withActor(UserId.of("alex")));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("jamie"), 0));

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("ghost"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void holdYourHorsesMarkingYourselfReadyOnceIsEnough() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, InitialiseGameParam.withActor(UserId.of("alex")));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("jamie"), 0));
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("jamie")));
        assertThat(ctx.player(UserId.of("jamie")).isReady()).isTrue();

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("jamie"))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("jamie");
    }

    @Test
    void gameStartsWhenAllPlayersAreReady() {
        final GameContext ctx = new TestGameContext();
        new InitialiseSimulatedGame().exec(ctx, InitialiseGameParam.withActor(UserId.of("alex")));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("alex"), 0));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("charlie"), 0));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("jules"), 1));
        new JoinTable().exec(ctx, JoinTableParam.withActor(UserId.of("quinn"), 1));

        new CheatDeal().exec(ctx, CheatDealParam.withActor(UserId.of("jules"), Sets.newHashSet(MahJong, B2)));

        assertThat(ctx.game()).describedAs("Nobody is ready").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("alex")));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 1 ready player").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("charlie")));
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("jules")));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 3 ready players").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.withActor(UserId.of("quinn")));
        assertThat(ctx.game()).describedAs("Should now be ready with all 4 ready players")
                .is(started);

        // We've given Mahjong to Jules, so we just check the trick is setup correctly. What a long method chain ...
        assertThat(ctx.game().currentRound().currentTrick().currentPlayer().id()).isEqualTo(UserId.of("jules"));
    }
}