package net.incongru.tichu.model;

import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static net.incongru.tichu.model.UserId.of;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

@ExtendWith(SoftAssertionsExtension.class)
class GameTest {

    private Players players;
    private Player alex, charlie, jules, quinn;

    @BeforeEach
    void setUp() {
        players = samplePlayers();
        alex = players.getPlayerById(of("Alex"));
        charlie = players.getPlayerById(of("Charlie"));
        jules = players.getPlayerById(of("Jules"));
        quinn = players.getPlayerById(of("Quinn"));
    }

    /**
     * Yeaaaah this is kind of an end-to-end test.
     */
    @Test
    void testBaseGameFlow(SoftAssertions softly) {
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
        softly.assertThat(alex.hand()).is(new Condition<>(hand -> hand.size() == 14, "has 14 cards"));
        softly.assertThat(charlie.hand()).is(new Condition<>(hand -> hand.size() == 14, "has 14 cards"));
        softly.assertThat(jules.hand()).is(new Condition<>(hand -> hand.size() == 14, "has 14 cards"));
        softly.assertThat(quinn.hand()).is(new Condition<>(hand -> hand.size() == 14, "has 14 cards"));
    }

    @Test
    void totalScoreCount() {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        game.finishRound(new FakeRound(game, new Score(20, 80)));
        game.finishRound(new FakeRound(game, new Score(50, 50)));
        game.finishRound(new FakeRound(game, new Score(30, 70)));

        assertThat(game.globalScore()).isEqualTo(new Score(100, 200));
    }

    @Test
    void scoreWithNoRoundPlayedShouldSimplyBeZeroZero() {
        final Players players = samplePlayers();
        final Game game = new Game(players, new TichuRules());
        assertThat(game.globalScore()).isEqualTo(new Score(0, 0));
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
