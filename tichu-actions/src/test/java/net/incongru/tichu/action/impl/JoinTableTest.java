package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.GameContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JoinTableTest {

    @Test
    void noMoreThan2PlayersCanJoinTeam() {
        final GameContext ctx = new TestGameContext();

        assertDoesNotThrow(() -> {
            new InitialiseGame().exec(ctx);
            new JoinTable("alex", 0).exec(ctx);
            new JoinTable("charlie", 0).exec(ctx);
        });

        assertThatThrownBy(() -> new JoinTable("incruste", 0).exec(ctx))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Team is complete");
    }

    @Test
    void playersCompleteWhenAllPlayersJoined() {
        final GameContext ctx = new TestGameContext();
        new InitialiseGame().exec(ctx);

        assertThat(ctx.game().players().isComplete()).describedAs("No played joined yet, shouldn't be ready")
                .isFalse();
        new JoinTable("alex", 0).exec(ctx);
        assertThat(ctx.game().players().isComplete()).describedAs("not complete with just 1 player")
                .isFalse();
        new JoinTable("charlie", 0).exec(ctx);
        new JoinTable("jules", 1).exec(ctx);
        assertThat(ctx.game().players().isComplete()).describedAs("not complete with just 3 players")
                .isFalse();
        new JoinTable("quinn", 1).exec(ctx);
        assertThat(ctx.game().players().isComplete()).describedAs("Should now be complete with all 4 players")
                .isTrue();
    }

}