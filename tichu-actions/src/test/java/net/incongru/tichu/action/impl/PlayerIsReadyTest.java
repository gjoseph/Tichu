package net.incongru.tichu.action.impl;

import com.google.common.collect.Sets;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
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
        new InitialiseGame().exec(ctx, InitialiseGameParam.with());
        new JoinTable().exec(ctx, JoinTableParam.with("jamie", 0));

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("ghost")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void holdYourHorsesMarkingYourselfReadyOnceIsEnough() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, InitialiseGameParam.with());
        new JoinTable().exec(ctx, JoinTableParam.with("jamie", 0));
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("jamie"));
        assertThat(ctx.player("jamie").isReady()).isTrue();

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("jamie")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("jamie");
    }

    @Test
    void gameStartsWhenAllPlayersAreReady() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, InitialiseGameParam.with());
        new JoinTable().exec(ctx, JoinTableParam.with("alex", 0));
        new JoinTable().exec(ctx, JoinTableParam.with("charlie", 0));
        new JoinTable().exec(ctx, JoinTableParam.with("jules", 1));
        new JoinTable().exec(ctx, JoinTableParam.with("quinn", 1));

        new CheatDeal().exec(ctx, CheatDealParam.with("jules", Sets.newHashSet(MahJong, B2)));

        assertThat(ctx.game()).describedAs("Nobody is ready").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("alex"));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 1 ready player").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("charlie"));
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("jules"));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 3 ready players").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, PlayerIsReadyParam.with("quinn"));
        assertThat(ctx.game()).describedAs("Should now be ready with all 4 ready players")
                .is(started);

        // We've given Mahjong to Jules, so we just check the trick is setup correctly. What a long method chain ...
        assertThat(ctx.game().currentRound().currentTrick().currentPlayer().name()).isEqualTo("jules");
    }
}