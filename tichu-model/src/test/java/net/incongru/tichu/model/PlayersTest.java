package net.incongru.tichu.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PlayersTest {
    @Test
    @Disabled("Currently doesn't work, but also not convinced by add/join impl")
    public void playerNamesMustBeUniqueCaseInsensitively() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Players players = new Players();
            players.join(new Player("Quinn"), new Team("t1"));
            players.join(new Player("Quinn"), new Team("t2"));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            final Players players = new Players();
            players.join(new Player("Quinn"), new Team("t1"));
            players.join(new Player("Jules"), new Team("t2"));
            players.join(new Player("quiNN"), new Team("t2"));
        });
    }

    @Test
    public void playerByNameIsCaseInsensitive() {
        final Players players = TestUtil.samplePlayers();

        assertThat(players.getPlayerByName("Salami")).isEmpty();
        assertThat(players.getPlayerByName("Charlie")).hasValue(players.getPlayer(1));
        assertThat(players.getPlayerByName("charlie")).hasValue(players.getPlayerByName("Charlie").get());
    }

    @Test
    @Disabled("Im not sure what this method was anymore")
    public void cycleFromNeedsBeToBeTestedWhatDoesItEvenDo() {
        final Players players = TestUtil.samplePlayers();
        final Player p1 = players.getPlayer(0);
        final Iterator<Player> playerIterator = players.cycleFrom(p1);
        fail();
    }

    @Test
    public void needs4PlayersToBeComplete() {
        final Players players = new Players();
        final Team t1 = new Team("Team 1");
        final Team t2 = new Team("Team 2");
        players.add(t1);
        players.add(t2);
        players.join(new Player("Alex"), t1);
        assertFalse(players.isComplete());
        players.join(new Player("Charlie"), t2);
        assertFalse(players.isComplete());
        players.join(new Player("Jules"), t2);
        assertFalse(players.isComplete());
        players.join(new Player("Quinn"), t1);
        assertTrue(players.isComplete());
    }
}