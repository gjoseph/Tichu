package net.incongru.tichu.model;

import lombok.Value;

/**
 * A round is a series of {@link Trick}s leading to the playing of all cards and scoring.
 */
public class Round {
    private final Game game;
    private Trick currentTrick;

    public Round(Game game) {
        this.game = game;
    }

    public Trick start() { // TODO why return Trick ?
        shuffleAndDeal();

        Players.Player firstPlayer = game.rules().whoStarts(game.players());
        return next(firstPlayer);
    }

    public Trick next(Players.Player whoStarts) {
        // TODO
        currentTrick = new Trick(game.rules(), game.players().cycleFrom(whoStarts), whoStarts);
        return currentTrick;
    }

    public void announce(Players.Player player, Announce announce) {
        final boolean canAnnounce = game.rules().canAnnounce(player, announce);
        if (!canAnnounce) {
            throw new IllegalStateException("Can't announce!?");
        }
        player.announce(announce);
    }

    public Score score() {
        // TODO not sure yet how to determine the round is done. 4th player will have cards left.
        return new Score(0, 0);
    }

    protected void shuffleAndDeal() {
        final Players players = game.players();

        for (int i = 1; i <= 4; i++) {
            players.getPlayer(i).reclaimCards();
        }

        final CardDeck cardDeck = game.rules().newShuffledDeck();

        // In reality, these loops would be inverted (per card, per player), but this helps controlling draft for simulations
        for (int p = 1; p <= 4; p++) {
            for (int c = 1; c <= 14; c++) {
                players.getPlayer(p).deal(cardDeck.take());
            }
        }
    }

    @Value
    public static class Score {
        private final int team1, team2;
    }
}
