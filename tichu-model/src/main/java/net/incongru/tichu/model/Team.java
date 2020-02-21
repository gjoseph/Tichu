package net.incongru.tichu.model;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 *
 */
public class Team {
    private final String name;
    private final Player[] players = new Player[2];

    public Team(String name) {
        Preconditions.checkNotNull(name, "Team name can't be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void join(Player p) {
        Objects.requireNonNull(name, "Player can't be null");

        if (players[0] == null) {
            players[0] = p;
        } else if (players[1] == null) {
            players[1] = p;
        } else {
            throw new IllegalStateException("Team is complete");
        }
    }

    /**
     * 0-based.
     */
    Player player(int index) {
        return players[index];
    }

    public boolean isComplete() {
        return players[0] != null && players[1] != null;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", player1=" + players[0] +
                ", player2=" + players[1] +
                '}';
    }
}
