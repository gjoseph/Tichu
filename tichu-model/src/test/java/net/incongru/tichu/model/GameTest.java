package net.incongru.tichu.model;

import org.assertj.core.api.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 */
public class GameTest {
    /**
     * Yeaaaah this is kind of an end-to-end test.
     */
    @Test
    public void testBaseGameFlow() {
        final Players players = new Players("Greg", "Rufus", "G-R", "Isa", "Catherine", "I-C");
        final Game game = new Game(players, new TichuRules());
        assertThat(game).has(new Condition<>(g -> g.isReadyToStart(), "ready to start"));
        final Round round = game.start();
        final Trick trick = round.start();
        assertTrue(game.isStarted());
        assertThat(game)
                .has(new Condition<>(g -> g.isStarted(), "isStarted"))
                .has(new Condition<>(g -> !g.isReadyToStart(), "isReadyToStart should be false"))
                .has(new Condition<>(g -> g.getFinishedRounds().size() == 0, "finished rounds=0"));

        // hands have been dealt
        assertThat(game.players().getPlayer(1).getHand()).hasSize(14);
        assertThat(game.players().getPlayer(2).getHand()).hasSize(14);
        assertThat(game.players().getPlayer(3).getHand()).hasSize(14);
        assertThat(game.players().getPlayer(4).getHand()).hasSize(14);
    }

    @Test
    public void totalScoreCount() {
        final Players players = new Players("Greg", "Rufus", "G-R", "Isa", "Catherine", "I-C");
        final Game game = new Game(players, new TichuRules());
        game.getFinishedRounds().add(new Game.FinishedRound(null, null, null, null, null, null, null, null, new Round.Score(20, 80), null)); // TODO This is not gonna fly, finishedRounds should be immutable
        game.getFinishedRounds().add(new Game.FinishedRound(null, null, null, null, null, null, null, null, new Round.Score(50, 50), null)); // TODO This is not gonna fly, finishedRounds should be immutable
        game.getFinishedRounds().add(new Game.FinishedRound(null, null, null, null, null, null, null, null, new Round.Score(30, 70), null)); // TODO This is not gonna fly, finishedRounds should be immutable

        assertThat(game.globalScore()).isEqualTo(new Round.Score(100, 200));
    }

}
