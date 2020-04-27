package net.incongru.tichu.action.impl;

import static net.incongru.tichu.model.HandAssert.assertThat;

import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.model.UserId;
import org.junit.jupiter.api.Test;

class InitialiseGameTest {

    @Test
    void automaticallyDeals14CardsToEachPlayer() {
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
        new PlayerIsReady()
            .exec(ctx, PlayerIsReadyParam.withActor(UserId.of("alex")));
        new PlayerIsReady()
            .exec(ctx, PlayerIsReadyParam.withActor(UserId.of("charlie")));
        new PlayerIsReady()
            .exec(ctx, PlayerIsReadyParam.withActor(UserId.of("jules")));
        new PlayerIsReady()
            .exec(ctx, PlayerIsReadyParam.withActor(UserId.of("quinn")));

        assertThat(ctx.player(UserId.of("alex")).hand()).hasSize(14);
        assertThat(ctx.player(UserId.of("charlie")).hand()).hasSize(14);
        assertThat(ctx.player(UserId.of("jules")).hand()).hasSize(14);
        assertThat(ctx.player(UserId.of("quinn")).hand()).hasSize(14);
    }
}
