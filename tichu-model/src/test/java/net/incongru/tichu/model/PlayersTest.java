package net.incongru.tichu.model;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static net.incongru.tichu.model.UserId.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayersTest {
    @Test
    void samePlayerCantJoinMultipleTimes() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Players players = new Players();
            players.join(new Player(of("Quinn")), new Team("t1"));
            players.join(new Player(of("Quinn")), new Team("t2"));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            final Players players = new Players();
            players.join(new Player(of("Quinn")), new Team("t1"));
            players.join(new Player(of("Jules")), new Team("t2"));
            players.join(new Player(of("Quinn")), new Team("t2"));
        });
    }

    @Test
    void cycleFromOrdersPlayersByTeam() {
        final Players players = new Players();
        final Team t1 = new Team("t1");
        final Team t2 = new Team("t2");
        players.add(t1);
        players.add(t2);

        final Player alex = new Player(of("Alex"));  // Quinn's team mate
        final Player charlie = new Player(of("Charlie")); // Jules' team mate
        final Player jules = new Player(of("Jules")); // Charlie's team mate
        final Player quinn = new Player(of("Quinn")); // Alex's team mate

        // Team membership influences play order, not join.
        // TODO we will probably decouple "joining" a game and deciding seat (i.e play order) later
        players.join(alex, t1);
        players.join(charlie, t2);
        players.join(jules, t2);
        players.join(quinn, t1);
        players.stream().forEach(Player::setReady);

        final Iterator<Player> it = players.cycleFrom(players.getPlayerById(of("Charlie")));
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
    void cycleFromFirstCallToNextGivesGivenPlayer() {
        final Players players = TestUtil.samplePlayers();
        final Player charlie = players.getPlayerById(of("Charlie"));
        // If we ask to "cycle from Charlie",
        final Iterator<Player> it = players.cycleFrom(charlie);
        // we expect the first call to it.next() to actually gives us back Charlie
        assertThat(it.next()).isEqualTo(charlie);
    }

    @Test
    void needs4PlayersToBeComplete() {
        final Players players = new Players();
        final Team t1 = new Team("Team 1");
        final Team t2 = new Team("Team 2");
        players.add(t1);
        players.add(t2);
        players.join(new Player(of("Alex")), t1);
        assertFalse(players.isComplete());
        players.join(new Player(of("Charlie")), t2);
        assertFalse(players.isComplete());
        players.join(new Player(of("Jules")), t2);
        assertFalse(players.isComplete());
        players.join(new Player(of("Quinn")), t1);
        assertTrue(players.isComplete());
    }
}