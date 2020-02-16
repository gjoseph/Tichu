package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.simu.Simulation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

public interface PostActionCommandFactory {

    Simulation.PostActionCommand expectSuccess();

    Simulation.PostActionCommand expectInvalidPlay(String expectedError); // TODO is there an enum of these?

    Simulation.PostActionCommand expectError(String expectedError);

    Simulation.PostActionCommand expectGameState(ExpectableGameState expectedGameState);

    Simulation.PostActionCommand expectPlay(TemporaryPlayNamesEnum play);

    Simulation.PostActionCommand expectWinTrick(); // TODO playerName ?

    Simulation.PostActionCommand expectEndOfRound();

    Simulation.PostActionCommand expectRoundScore(String teamName, int expectedScore);

    Simulation.PostActionCommand debugPlayerHand(String playerName);

    static interface NameableEnum {
        static <E extends Enum<E> & NameableEnum> E byName(Class<E> enumClass, String name) {
            for (E p : enumClass.getEnumConstants()) {
                if (name.equalsIgnoreCase(p.name())) {
                    return p;
                }
                if (p.altNames().contains(name.toLowerCase())) {
                    return p;
                }
            }
            return null;
        }

        List<String> altNames();
    }

    // TODO this should go elsewhere
    static enum TemporaryPlayNamesEnum implements NameableEnum {
        Single, Pair, Triplet,
        FullHouse("full house"),
        Straight("street"),
        ConsecutivePairs("consecutive pairs"),
        BombOf4("bomb of 4", "bomb of four"),
        StraightBomb("straight bomb", "street bomb");

        private final List<String> altNames;

        TemporaryPlayNamesEnum(String... altNames) {
            this.altNames = Arrays.asList(altNames);
        }

        @Override
        public List<String> altNames() {
            return altNames;
        }

    }

    enum ExpectableGameState implements NameableEnum, Predicate<Game> {
        Started(Game::isStarted),
        NotStarted(not(Started.predicate), "not started"),
        ReadyToStart(Game::isReadyToStart, "ready", "ready to start"),
        NotReadyToStart(not(ReadyToStart.predicate), "not ready", "not ready to start");

        private final Predicate<Game> predicate;
        private final List<String> altNames;

        ExpectableGameState(Predicate<Game> predicate, String... altNames) {
            this.predicate = predicate;
            this.altNames = Arrays.asList(altNames);
        }

        @Override
        public boolean test(Game g) {
            return predicate.test(g);
        }

        @Override
        public List<String> altNames() {
            return altNames;
        }
    }
}
