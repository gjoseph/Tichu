package net.incongru.tichu.model;

import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

@ExtendWith(SoftAssertionsExtension.class)
public class GameTest {
    /**
     * Yeaaaah this is kind of an end-to-end test.
     */
    @Test
    public void testBaseGameFlow(SoftAssertions softly) {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        assertThat(game).is(new Condition<>(Game::isReadyToStart, "ready to start"));
        final Round round = game.start();
        final Trick trick = round.start();
        assertThat(game)
                .is(new Condition<>(Game::isStarted, "isStarted"))
                .is(not(new Condition<>(Game::isReadyToStart, "isReadyToStart")))
                .extracting(Game::finishedRounds, as(InstanceOfAssertFactories.list(Game.FinishedRound.class)))
                .hasSize(0);

        // hands have been dealt
        softly.assertThat(game.players().getPlayer(0).hand()).hasSize(14);
        softly.assertThat(game.players().getPlayer(1).hand()).hasSize(14);
        softly.assertThat(game.players().getPlayer(2).hand()).hasSize(14);
        softly.assertThat(game.players().getPlayer(3).hand()).hasSize(14);
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
