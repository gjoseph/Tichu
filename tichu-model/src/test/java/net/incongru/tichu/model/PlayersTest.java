package net.incongru.tichu.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThat(players.getPlayerByName("charlie")).hasValue(players.getPlayerByName("Charlie").get());
        assertThat(players.getPlayerByName("chaRLie")).hasValue(players.getPlayerByName("charliE").get());
    }

    @Test
    public void cycleFromOrdersPlayersByTeam() {
        final Players players = new Players();
        final Team t1 = new Team("t1");
        final Team t2 = new Team("t2");
        players.add(t1);
        players.add(t2);

        final Player alex = new Player("Alex");  // Quinn's team mate
        final Player charlie = new Player("Charlie"); // Jules' team mate
        final Player jules = new Player("Jules"); // Charlie's team mate
        final Player quinn = new Player("Quinn"); // Alex's team mate

        // Team membership influences play order, not join.
        // TODO we will probably decouple "joining" a game and deciding seat (i.e play order) later
        players.join(alex, t1);
        players.join(charlie, t2);
        players.join(jules, t2);
        players.join(quinn, t1);
        players.stream().forEach(Player::setReady);

        final Iterator<Player> it = players.cycleFrom(players.getPlayerByName("Charlie").get());
        // Start at Charlie, team 2
        assertThat(it.next()).isEqualTo(charlie);
        // Next up should be a player from other team; which of the 2 will for now be determined by join order (i.e who joined team 1 after charlie joined)
        assertThat(it.next()).isEqualTo(quinn);
        // Back to team 2
        assertThat(it.next()).isEqualTo(jules);
        // Team 1
        assertThat(it.next()).isEqualTo(alex);
        // And back to start player
        assertThat(it.next()).isEqualTo(charlie);
    }

    @Test
    public void cycleFromFirstCallToNextGivesGivenPlayer() {
        final Players players = TestUtil.samplePlayers();
        final Player charlie = players.getPlayerByName("Charlie").orElseThrow();
        // If we ask to "cycle from Charlie",
        final Iterator<Player> it = players.cycleFrom(charlie);
        // we expect the first call to it.next() to actually gives us back Charlie
        assertThat(it.next()).isEqualTo(charlie);
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