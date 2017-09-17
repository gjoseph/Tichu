package net.incongru.tichu.model;

import com.google.common.base.Preconditions;

/**
 *
 */
public class Team {
    private final String name;
    private final Player player1;
    private final Player player2;

    public Team(String name, Player player1, Player player2) {
        Preconditions.checkNotNull(name, "Team name can't be null");
        Preconditions.checkNotNull(player1, "Player 1 for team %s can't be null", name);
        Preconditions.checkNotNull(player2, "Player 2 for team %s can't be null", name);
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", player1=" + player1 +
                ", player2=" + player2 +
                '}';
    }
}
