package net.incongru.tichu.model;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * A round is a series of {@link Trick}s leading to the playing of all cards and scoring.
 */
public class Round {
    private final Game game;
    private final List<Game.Announced> announces;
    private Trick currentTrick;

    public Round(Game game) {
        this.game = game;
        this.announces = new ArrayList<>(4);
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
        // TODO check if this player already announced
        announces.add(new Game.Announced(player, announce));
    }

    public Trick currentTrick() {
        return currentTrick;
    }

    public boolean isDone() {
        // If more than a player still have hand cards, the round isn't done
        return game.players().stream().filter(Functions.EMPTY_HANDED).count() <= 1;
    }

    public Score score() {
        // We could count score before round is done, but isn't that cheating ?
        if (!isDone()) {
            throw new IllegalStateException("Can't count score before the round is done");
        }
//        final Map<Players.Player, Integer> scoresByPlayer = game.players().stream().collect(
//                Collectors.groupingBy(
//                        Function.identity(),
//                        ));
        throw new IllegalStateException("not impl yet");
    }

    public List<Game.AnnounceMade> announces() {
        if (!isDone()) {
            throw new IllegalStateException("Can't count announces made before the round is done");
        }
        throw new IllegalStateException("not impl yet");
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
