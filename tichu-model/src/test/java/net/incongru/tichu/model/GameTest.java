package net.incongru.tichu.model;

import static java.lang.Boolean.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author gjoseph
 */
public class GameTest {
    /**
     * Yeaaaah this is kind of an end-to-end test.
     */
    @Test
    public void testBaseGameFlow() {
        final Game game = new Game(new TichuRules());
        try {
            game.start();
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), containsString("Not ready"));
        }

        final Game.Player p1 = new Game.Player("Greg");
        final Game.Player p2 = new Game.Player("Isa");
        final Game.Player p3 = new Game.Player("Rufus");
        final Game.Player p4 = new Game.Player("Catherine");
        Game.Team t1 = new Game.Team("G-R");
        Game.Team t2 = new Game.Team("I-C");
        t1.setPlayer1(p1);
        t1.setPlayer2(p2);
        t2.setPlayer1(p3);
        t2.setPlayer2(p4);
        game.setTeam1(t1);
        game.setTeam2(t2);

        assertThat(game, hasProperty("readyToStart", is(TRUE)));
        final Round round = game.start();

        assertTrue(game.isStarted());
        assertThat(game, allOf(
                hasProperty("isStarted", is(TRUE)),
                hasProperty("readyToStart", is(FALSE)),
                hasProperty("finishedRounds", iterableWithSize(0)),
                hasProperty("currentPlays", iterableWithSize(0))
        ));

        // hands have been dealt
        assertThat(game.getTeam1().getPlayer1().getHand(), hasSize(14));
        assertThat(game.getTeam1().getPlayer2().getHand(), hasSize(14));
        assertThat(game.getTeam2().getPlayer1().getHand(), hasSize(14));
        assertThat(game.getTeam2().getPlayer2().getHand(), hasSize(14));


    }

}
