package net.incongru.tichu.model;

import com.google.common.collect.Sets;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(
                trick.play(players.getPlayer(0), Sets.newHashSet(DeckConstants.B2)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);

        // players 2 and 3 pass, we're not done yet
        assertThat(
                trick.play(players.getPlayer(1), Sets.newHashSet()).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertThat(
                trick.play(players.getPlayer(2), Sets.newHashSet()).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertFalse(trick.isDone());

        // 4th player passes, we're done
        assertThat(
                trick.play(players.getPlayer(3), Sets.newHashSet()).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
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

    @Test
    public void prevPlayReturnsPreviousNonPassPlay() {
        final Players players = samplePlayers();
        final Trick trick = newTrick(players);
        players.getPlayer(0).deal(DeckConstants.B2);
        players.getPlayer(3).deal(DeckConstants.G5);

        // 1st player plays
        assertThat(
                trick.play(players.getPlayer(0), Sets.newHashSet(DeckConstants.B2)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);

        // players 2 and 3 pass, we're not done yet
        assertThat(
                trick.play(players.getPlayer(1), Sets.newHashSet()).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertThat(
                trick.play(players.getPlayer(2), Sets.newHashSet()).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertFalse(trick.isDone());

        // previous non-pass play is player 1's single
        assertThat(trick.previousNonPass().getCards()).isEqualTo(Collections.singleton(DeckConstants.B2));
        // player 4 plays another single, that's valid
        assertThat(
                trick.play(players.getPlayer(3), Sets.newHashSet(DeckConstants.G5)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertFalse(trick.isDone());
    }

    private Trick newTrick(Players players) {
        final Player p1 = players.getPlayer(0);
        return new Trick(tichuRules, players.cycleFrom(p1), p1);
    }

}
