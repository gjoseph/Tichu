package net.incongru.tichu.model;

import static net.incongru.tichu.model.card.CardPredicates.is;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardSpecials;
import net.incongru.tichu.model.plays.BombOf4;
import net.incongru.tichu.model.plays.InvalidPlay;
import net.incongru.tichu.model.plays.Pair;
import net.incongru.tichu.model.plays.Pass;
import net.incongru.tichu.model.plays.Single;
import net.incongru.tichu.model.plays.Straight;
import net.incongru.tichu.model.plays.Triple;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

/**
 * Models most (all?) rules of the game, such that, maybe, one day (...) we can make this an interface and swap out some rules. Whatever.
 * TODO a lot of the "rules" are actually encoded in the Trick class. Move them over here?
 */
public class TichuRules {

    private final List<Play.PlayFactory<?>> factories;

    public TichuRules() {
        this.factories = makeFactories();
    }

    // If rules were to change, one would not only subclass TichuRules, but probably provide different factories too.
    protected List<Play.PlayFactory<?>> makeFactories(
        @UnderInitialization TichuRules this
    ) {
        return Arrays.asList(
            new Pass.Factory(),
            new Single.Factory(),
            new Pair.Factory(),
            new Triple.Factory(),
            new BombOf4.Factory(),
            new Straight.Factory()
        );
    }

    public CardDeck newShuffledDeck() {
        return new CardDeck();
    }

    public Player whoStarts(Players players) {
        return players
            .stream()
            .filter(hasMahjong)
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException("No player has the Mahjong!?")
            );
    }

    private static final Predicate<Player> hasMahjong = player ->
        player.hand().has(is(CardSpecials.MahJong));

    @Nonnull
    public Play validate(Set<Card> cards) {
        for (Play.PlayFactory factory : factories) {
            Play candidate = factory.is(cards);
            if (candidate != null) {
                return candidate;
            }
        }
        return new InvalidPlay(cards);
    }

    public boolean isBomb(Play play) {
        return play.isBomb();
    }

    public boolean isValid(Play play) {
        // check the play even makes sense (no identical cards)
        // maybe it needs the deck or game to know what cards have already been discarded etc
        // TODO is this needed? dont we trust the Play.Factories? Should Play have a isValid() method?
        return !(play instanceof InvalidPlay);
    }

    public boolean canPlayAfter(Play before, Play after) {
        if (!isValid(after)) {
            throw new IllegalStateException(
                "Learn the rules. Call isValid() before canPlayAfter()."
            );
        }
        return after.canBePlayedAfter(before);
    }

    public boolean canAnnounce(Player player, Announce announce) {
        switch (announce) {
            case TICHU:
                return player.hand().size() == 14;
            case BIG_TICHU:
                throw new UnsupportedOperationException(
                    "Big Tichu not supported yet"
                );
        }
        throw new IllegalStateException(
            announce + " is not a known Announce!?"
        );
    }
}
