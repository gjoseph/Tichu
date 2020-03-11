package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JoinTableTest {

    @Test
    void noMoreThan2PlayersCanJoinTeam() {
        final GameContext ctx = new TestGameContext();

        assertDoesNotThrow(() -> {
            new InitialiseGame().exec(ctx, InitialiseGameParam.with());
            new JoinTable().exec(ctx, JoinTableParam.with("alex", 0));
            new JoinTable().exec(ctx, JoinTableParam.with("charlie", 0));
        });

        assertThatThrownBy(() -> new JoinTable().exec(ctx, JoinTableParam.with("incruste", 0)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Team is complete");
    }

    @Test
    void playersCompleteWhenAllPlayersJoined() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx, InitialiseGameParam.with());

        assertThat(ctx.game().players().isComplete()).describedAs("No played joined yet, shouldn't be ready")
                .isFalse();
        new JoinTable().exec(ctx, JoinTableParam.with("alex", 0));
        assertThat(ctx.game().players().isComplete()).describedAs("not complete with just 1 player")
                .isFalse();
        new JoinTable().exec(ctx, JoinTableParam.with("charlie", 0));
        new JoinTable().exec(ctx, JoinTableParam.with("jules", 1));
        assertThat(ctx.game().players().isComplete()).describedAs("not complete with just 3 players")
                .isFalse();
        new JoinTable().exec(ctx, JoinTableParam.with("quinn", 1));
        assertThat(ctx.game().players().isComplete()).describedAs("Should now be complete with all 4 players")
                .isTrue();
    }

}