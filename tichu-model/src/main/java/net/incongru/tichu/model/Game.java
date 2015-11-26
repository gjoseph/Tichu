package net.incongru.tichu.model;

import lombok.Getter;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

/**
 * A Game is a series of {@link Round}s leading to one team winning by scoring 1000 points or more
 * (TODO: the winning condition should be configurable per game)
 */
public class Game {
    private final TichuRules rules;
    private final Players players;

    @Getter
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

        return next();
    }

    public Round next() {
        if (!started) {
            throw new IllegalStateException("Not started");
        }

        currentRound = new Round(this);
        return currentRound;
    }

    public TichuRules rules() {
        return rules;
    }

    public Players players() {
        return players;
    }

    public Round.Score globalScore() {
        return finishedRounds.stream().map(FinishedRound::getScore).reduce((score1, score2) -> new Round.Score(score1.getTeam1() + score2.getTeam1(), score1.getTeam2() + score2.getTeam2())).get();
    }

    @Value
    static public class FinishedRound {
        private Announce announcePlayer1, announcePlayer2, announcePlayer3, announcePlayer4;
        private Boolean announceMetPlayer1, announceMetPlayer2, announceMetPlayer3, announceMetPlayer4;
        private Round.Score score;
        private Players.Player finishingPlayer;
    }

}
