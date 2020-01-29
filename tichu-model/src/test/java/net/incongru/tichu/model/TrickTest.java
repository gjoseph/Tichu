package net.incongru.tichu.model;

import com.google.common.collect.Sets;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TrickTest {

    private final TichuRules tichuRules = new TichuRules();

    @Test
    public void isNotDoneIfNotEveryBodyPassed() {
        final Players players = newPlayers();
        final Trick trick = newTrick(players);
        players.getPlayer(1).deal(DeckConstants.B2);

        // 1st player plays
        trick.play(players.getPlayer(1), Sets.newHashSet(DeckConstants.B2));

        // players 2 and 3 pass, we're not done yet
        trick.play(players.getPlayer(2), Sets.newHashSet());
        trick.play(players.getPlayer(3), Sets.newHashSet());
        assertFalse(trick.isDone());

        // 4th player passes, we're done
        trick.play(players.getPlayer(4), Sets.newHashSet());
        assertTrue(trick.isDone());
    }

    @Test
    public void allPassIsNotDone() {
        final Players players = newPlayers();
        final Trick trick = newTrick(players);
        trick.play(players.getPlayer(1), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(2), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(3), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(4), Sets.newHashSet());
        assertFalse(trick.isDone());
    }

    protected Trick newTrick(Players players) {
        final Players.Player p1 = players.getPlayer(1);
        return new Trick(tichuRules, players.cycleFrom(p1), p1);
    }

    protected Players newPlayers() {
        return new Players("p1", "p3", "t13", "p2", "p4", "t24");
    }

}
