package net.incongru.tichu.model;

public class TestUtil {
    static Players samplePlayers() {
        final Players players = new Players();
        final Team t1 = new Team("t1");
        final Team t2 = new Team("t2");
        players.add(t1);
        players.add(t2);
        players.join(new Player("Alex"), t1);
        players.join(new Player("Charlie"), t1);
        players.join(new Player("Jules"), t2);
        players.join(new Player("Quinn"), t2);
        players.stream().forEach(Player::setReady);
        return players;
    }
}
