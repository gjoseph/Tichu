package net.incongru.tichu.model;

import com.google.common.collect.Sets;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.Test;

import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrickTest {

    private final TichuRules tichuRules = new TichuRules();

    @Test
    public void isNotDoneIfNotEveryBodyPassed() {
        final Players players = samplePlayers();
        final Trick trick = newTrick(players);
        players.getPlayer(0).deal(DeckConstants.B2);

        // 1st player plays
        trick.play(players.getPlayer(0), Sets.newHashSet(DeckConstants.B2));

        // players 2 and 3 pass, we're not done yet
        trick.play(players.getPlayer(1), Sets.newHashSet());
        trick.play(players.getPlayer(2), Sets.newHashSet());
        assertFalse(trick.isDone());

        // 4th player passes, we're done
        trick.play(players.getPlayer(3), Sets.newHashSet());
        assertTrue(trick.isDone());
    }

    @Test
    public void allPassIsNotDone() {
        final Players players = samplePlayers();
        final Trick trick = newTrick(players);
        trick.play(players.getPlayer(0), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(1), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(2), Sets.newHashSet());
        assertFalse(trick.isDone());
        trick.play(players.getPlayer(3), Sets.newHashSet());
        assertFalse(trick.isDone());
    }

    private Trick newTrick(Players players) {
        final Player p1 = players.getPlayer(0);
        return new Trick(tichuRules, players.cycleFrom(p1), p1);
    }

}
