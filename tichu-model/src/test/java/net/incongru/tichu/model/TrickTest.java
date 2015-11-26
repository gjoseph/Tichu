package net.incongru.tichu.model;

import com.google.common.collect.Sets;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class TrickTest {

    private final TichuRules tichuRules = new TichuRules();

    @Test
    public void isNotDoneIfNotEveryBodyPassed() {
        final Players players = newPlayers();
        final Trick trick = newTrick(players);
        trick.play(players.getPlayer(1), newPlay(DeckConstants.B2));
        trick.play(players.getPlayer(2), newPlay());
        trick.play(players.getPlayer(3), newPlay());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(4), newPlay());
        assertTrue(trick.isDone());
    }

    @Test
    public void allPassIsNotDone() {
        final Players players = newPlayers();
        final Trick trick = newTrick(players);
        trick.play(players.getPlayer(1), newPlay());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(2), newPlay());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(3), newPlay());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(4), newPlay());
        assertFalse(trick.isDone());
    }

    protected Trick newTrick(Players players) {
        final Players.Player p1 = players.getPlayer(1);
        return new Trick(tichuRules, players.cycleFrom(p1), p1);
    }

    protected Players newPlayers() {
        return new Players("p1", "p3", "t13", "p2", "p4", "t24");
    }

    private Play newPlay(Card... cards) {
        return tichuRules.validate(Sets.newHashSet(cards));
    }
}
