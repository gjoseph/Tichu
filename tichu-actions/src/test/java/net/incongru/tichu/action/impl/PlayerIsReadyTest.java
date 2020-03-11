package net.incongru.tichu.action.impl;

import com.google.common.collect.Sets;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.impl.CheatDeal.CheatDealParam;
import net.incongru.tichu.action.impl.InitialiseGame.InitialiseGameParam;
import net.incongru.tichu.action.impl.JoinTable.JoinTableParam;
import net.incongru.tichu.action.impl.PlayerIsReady.PlayerIsReadyParam;
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
        new InitialiseGame().exec(ctx, new InitialiseGameParam());
        new JoinTable().exec(ctx, new JoinTableParam("jamie", 0));

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("ghost")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void holdYourHorsesMarkingYourselfReadyOnceIsEnough() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, new InitialiseGameParam());
        new JoinTable().exec(ctx, new JoinTableParam("jamie", 0));
        new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("jamie"));
        assertThat(ctx.player("jamie").isReady()).isTrue();

        assertThatThrownBy(() -> new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("jamie")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("jamie");
    }

    @Test
    void gameStartsWhenAllPlayersAreReady() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, new InitialiseGameParam());
        new JoinTable().exec(ctx, new JoinTableParam("alex", 0));
        new JoinTable().exec(ctx, new JoinTableParam("charlie", 0));
        new JoinTable().exec(ctx, new JoinTableParam("jules", 1));
        new JoinTable().exec(ctx, new JoinTableParam("quinn", 1));

        new CheatDeal().exec(ctx, new CheatDealParam("jules", Sets.newHashSet(MahJong, B2)));

        assertThat(ctx.game()).describedAs("Nobody is ready").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("alex"));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 1 ready player").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("charlie"));
        new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("jules"));
        assertThat(ctx.game()).describedAs("Should still not be ready with just 3 ready players").is(notReadyNorStarted);
        new PlayerIsReady().exec(ctx, new PlayerIsReadyParam("quinn"));
        assertThat(ctx.game()).describedAs("Should now be ready with all 4 ready players")
                .is(started);

        // We've given Mahjong to Jules, so we just check the trick is setup correctly. What a long method chain ...
        assertThat(ctx.game().currentRound().currentTrick().currentPlayer().name()).isEqualTo("jules");
    }
}