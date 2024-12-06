package net.incongru.tichu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A round is a series of {@link Trick}s leading to the playing of all cards and scoring.
 */
public class Round {

    private final Game game;
    private final List<Announced> announces;
    private @Nullable Trick currentTrick;

    public Round(Game game) {
        this.game = game;
        this.announces = new ArrayList<>(4);
    }

    public Trick start() { // TODO why return Trick ?
        shuffleAndDeal();

        Player firstPlayer = game.rules().whoStarts(game.players());
        return newTrick(firstPlayer);
    }

    private Trick newTrick(Player whoStarts) {
        if (currentTrick != null && !currentTrick.isDone()) {
            throw new IllegalStateException("Trick in progress");
        }
        // TODO test!!
        currentTrick = new Trick(
            game.rules(),
            game.players().cycleFrom(whoStarts)
        );
        return currentTrick;
    }

    public Trick newTrick() {
        if (currentTrick == null) {
            throw new IllegalStateException("Game not started");
        }
        // TODO test!!
        final Player winnerOfLast = currentTrick.previousNonPass().player();
        return newTrick(winnerOfLast);
    }

    public void announce(Player player, Announce announce) {
        final boolean canAnnounce = game.rules().canAnnounce(player, announce);
        if (!canAnnounce) {
            throw new IllegalStateException("Can't announce!?");
        }
        // TODO check if this player already announced
        announces.add(new Announced(player, announce));
    }

    public @Nullable Trick currentTrick() {
        return currentTrick;
    }

    public boolean isDone() {
        // If more than a player still have hand cards, the round isn't done
        return (
            game.players().stream().filter(Functions.EMPTY_HANDED).count() <= 1
        );
    }

    public Score score() {
        // We could count score before round is done, but isn't that cheating ?
        if (!isDone()) {
            throw new IllegalStateException(
                "Can't count score before the round is done"
            );
        }
        //        final Map<Players.Player, Integer> scoresByPlayer = game.players().stream().collect(
        //                Collectors.groupingBy(
        //                        Function.identity(),
        //                        ));
        throw new IllegalStateException("not impl yet");
    }

    List<AnnounceResult> announces() {
        if (!isDone()) {
            throw new IllegalStateException(
                "Can't count announces made before the round is done"
            );
        }
        return announces
            .stream()
            .map(announced ->
                new AnnounceResult(
                    announced.player(),
                    announced.announce(),
                    true/*TODO everybody wins*/
                )
            )
            .collect(Collectors.toList());
    }

    protected void shuffleAndDeal() {
        final Players players = game.players();

        players.stream().forEach(p -> p.reclaimCards());

        final CardDeck cardDeck = game.rules().newShuffledDeck();

        // In reality, these loops would be inverted (per card, per player), but this helps controlling draft for simulations
        // Also, TODO this is drafting in join-order (see Players#players), so not 100% correct wrt rules
        players
            .stream()
            .forEach(player -> {
                for (int c = 0; c < 14; c++) {
                    player.deal(cardDeck.take());
                }
            });
    }
}
