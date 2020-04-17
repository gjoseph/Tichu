package net.incongru.tichu.model;

import static net.incongru.tichu.model.UserId.of;

class TestUtil {
    static Players samplePlayers() {
        final Players players = new Players();
        final Team t1 = new Team("t1");
        final Team t2 = new Team("t2");
        players.add(t1);
        players.add(t2);
        // join order should not influence play order! For testing reasons, we'll keep play order alphabetical
        players.join(new Player(of("Alex")), t1); // Jules' team mate
        players.join(new Player(of("Jules")), t1); // Alex's team mate
        players.join(new Player(of("Charlie")), t2); // Quinn's team mate
        players.join(new Player(of("Quinn")), t2); // Charlie's team mate
        players.stream().forEach(Player::setReady);
        return players;
    }
}
