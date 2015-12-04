package net.incongru.tichu.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

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

    @Getter
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

    public Round.Score globalScore() {
        return finishedRounds.stream()
                .map(FinishedRound::getScore)
                .reduce((score1, score2) -> new Round.Score(score1.getTeam1() + score2.getTeam1(), score1.getTeam2() + score2.getTeam2()))
                .orElse(new Round.Score(0, 0));
    }

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static public class FinishedRound {
        private final List<AnnounceMade> announces;
        private final Round.Score score;
        private final Players.Player finishingPlayer;

        public FinishedRound(Round round) {
            this(round.announces(), round.score(), null);//TODO
        }
    }

    /**
     * During play, the announce is made and pending.
     */
    @Value
    static public class Announced {
        private final Players.Player player;
        private final Announce announce;
    }

    /**
     * After play, the announce was met or failed.
     */
    @Value
    static public class AnnounceMade {
        private final Players.Player player;
        private final Announce announce;
        private final Boolean result;
    }

}
