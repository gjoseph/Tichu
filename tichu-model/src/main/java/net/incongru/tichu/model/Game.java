package net.incongru.tichu.model;

import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

/**
 * A Game is a series of {@link Round}s leading to one team winning by scoring 1000 points or more
 * (TODO: the winning condition should be configurable per game)
 */
@Data
public class Game {
    private final TichuRules rules;
    private Team team1;
    private Team team2;

    @Setter(AccessLevel.NONE) // No setter here, handled manually
    private boolean started;

    private final List<FinishedRound> finishedRounds;

    // current round:
    private final Deque<Trick.Play> currentPlays;
    private Announce announcePlayer1, announcePlayer2, announcePlayer3, announcePlayer4;

    public Game(TichuRules rules) {
        this.rules = rules;
        this.finishedRounds = new LinkedList<>();
        this.currentPlays = new LinkedList<>();
    }

    public boolean isReadyToStart() {
        return !started && team1 != null && team2 != null && currentPlays.isEmpty() && finishedRounds.isEmpty();
    }

    public void start() {
        if (!isReadyToStart()) {
            throw new IllegalStateException("Not ready to start");
        }
        started = true;
    }

    @Value
    static public class FinishedRound {
        private Announce announcePlayer1, announcePlayer2, announcePlayer3, announcePlayer4;
        private Boolean announceMetPlayer1, announceMetPlayer2, announceMetPlayer3, announceMetPlayer4;
        private int scoreTeam1, scoreTeam2;
        private Player finishingPlayer;
    }

    static enum Announce {tichu, bigTichu}

    static void validate(Team t, int teamPosition) {
        Preconditions.checkArgument(teamPosition == 1 || teamPosition == 2, "Team position must be 1 or 2 (was " + teamPosition + ")");
        Preconditions.checkNotNull(t, "Team %s is not set", teamPosition);
        Preconditions.checkArgument(t.getPlayer1().getPosition() == 0 + teamPosition, "Position of player 1 in team %s is not correct (was %s)", teamPosition, t.getPlayer1().getPosition());
        Preconditions.checkArgument(t.getPlayer2().getPosition() == 1 + teamPosition, "Position of player 2 in team %s is not correct (was %s)", teamPosition, t.getPlayer2().getPosition());
    }

    @Data
    static public class Team {
        private final String name;
        private Player player1;
        private Player player2;
    }

    @Data
    static public class Player {
        private final Set<Card> hand = new LinkedHashSet<>();
        private final String name;
        /* 1 to 4 */ // TODO or should we assume this ?
        int position;
    }
}
