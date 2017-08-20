package net.incongru.tichu.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A Game is a series of {@link Round}s leading to one team winning by scoring 1000 points or more
 * (TODO: the winning condition should be configurable per game)
 */
public class Game {
    private final TichuRules rules;
    private final Players players;
    private final List<FinishedRound> finishedRounds;
    private boolean started;

    private Round currentRound;

    public Game(Players players, TichuRules rules) {
        this.players = players;
        this.rules = rules;
        this.finishedRounds = new LinkedList<>();
    }

    public boolean isReadyToStart() {
        return !started && currentRound == null && finishedRounds.isEmpty();
    }

    public Round start() {
        if (!isReadyToStart()) {
            throw new IllegalStateException("Not ready to start");
        }
        started = true;
        currentRound = new Round(this);

        return currentRound;
    }

    // Originally, we intended for clients to only access current round via start() or next() but that doesn't seem to be convenient
    // they would need to keep the ref to round themselves, and make sure to update it when calling next()
    public Round currentRound() {
        return currentRound;
    }

    public Round next() {
        if (!started) {
            throw new IllegalStateException("Not started");
        }
        finishedRounds.add(new FinishedRound(currentRound));
        currentRound = new Round(this);
        return currentRound;
    }

    public TichuRules rules() {
        return rules;
    }

    public Players players() {
        return players;
    }

    public List<FinishedRound> finishedRounds() {
        return Collections.unmodifiableList(finishedRounds);
    }

    @VisibleForTesting
    boolean isStarted() {
        return started;
    }

    public Round.Score globalScore() {
        return finishedRounds.stream()
                .map(FinishedRound::getScore)
                .reduce((score1, score2) -> new Round.Score(score1.getTeam1() + score2.getTeam1(), score1.getTeam2() + score2.getTeam2()))
                .orElse(new Round.Score(0, 0));
    }

    static public class FinishedRound {
        private final List<AnnounceMade> announces;
        private final Round.Score score;
        private final Players.Player finishingPlayer;

        FinishedRound(Round round) {
            this(round.announces(), round.score(), null);//TODO
        }

        @VisibleForTesting
        FinishedRound(List<AnnounceMade> announces, Round.Score score, Players.Player finishingPlayer) {
            this.announces = announces;
            this.score = score;
            this.finishingPlayer = finishingPlayer;
        }

        public Round.Score getScore() {
            return score;
        }
    }

    /**
     * During play, the announce is made and pending.
     */
    static public class Announced {
        private final Players.Player player;
        private final Announce announce;

        public Announced(Players.Player player, Announce announce) {
            this.player = player;
            this.announce = announce;
        }
    }

    /**
     * After play, the announce was met or failed.
     */
    static public class AnnounceMade {
        private final Players.Player player;
        private final Announce announce;
        private final Boolean result;

        public AnnounceMade(Players.Player player, Announce announce, Boolean result) {
            this.player = player;
            this.announce = announce;
            this.result = result;
        }
    }

}
