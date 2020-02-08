package net.incongru.tichu.model;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    /**
     * Yeaaaah this is kind of an end-to-end test.
     */
    @Test
    public void testBaseGameFlow() {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        assertThat(game).has(new Condition<>(g -> g.isReadyToStart(), "ready to start"));
        final Round round = game.start();
        final Trick trick = round.start();
        assertTrue(game.isStarted());
        assertThat(game)
                .has(new Condition<>(g -> g.isStarted(), "isStarted"))
                .has(new Condition<>(g -> !g.isReadyToStart(), "isReadyToStart should be false"))
                .has(new Condition<>(g -> g.finishedRounds().size() == 0, "finished rounds=0"));

        // hands have been dealt
        assertThat(game.players().getPlayer(1).hand()).hasSize(14);
        assertThat(game.players().getPlayer(2).hand()).hasSize(14);
        assertThat(game.players().getPlayer(3).hand()).hasSize(14);
        assertThat(game.players().getPlayer(4).hand()).hasSize(14);
    }

    @Test
    public void totalScoreCount() {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        game.finishRound(new FakeRound(game, ImmutableScore.of(20, 80)));
        game.finishRound(new FakeRound(game, ImmutableScore.of(50, 50)));
        game.finishRound(new FakeRound(game, ImmutableScore.of(30, 70)));

        assertThat(game.globalScore()).isEqualTo(ImmutableScore.of(100, 200));
    }

    @Test
    public void scoreWithNoRoundPlayedShouldSimplyBeZeroZero() {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        assertThat(game.globalScore()).isEqualTo(ImmutableScore.of(0, 0));
    }

    private static class FakeRound extends Round {
        private final Score fixedScore;

        public FakeRound(Game game, Score score) {
            super(game);
            this.fixedScore = score;
        }

        @Override
        public Score score() {
            return fixedScore;
        }
    }
}
