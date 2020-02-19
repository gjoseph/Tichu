package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.simu.Simulation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface PostActionCommandFactory {
    Simulation.PostActionCommand expectSuccess();

    Simulation.PostActionCommand expectInvalidPlay(String expectedError); // TODO is there an enum of these?

    Simulation.PostActionCommand expectError(String expectedError);

    Simulation.PostActionCommand expectPlay(TemporaryPlayNamesEnum play);

    Simulation.PostActionCommand expectWinTrick(); // TODO playerName ?

    Simulation.PostActionCommand expectEndOfRound();

    Simulation.PostActionCommand expectRoundScore(String teamName, int expectedScore);

    Simulation.PostActionCommand debugPlayerHand(String playerName);

    // TODO this should go elsewhere
    static enum TemporaryPlayNamesEnum {
        Single, Pair, Triplet,
        FullHouse("full house"),
        Straight("street"),
        ConsecutivePairs("consecutive pairs"),
        BombOf4("bomb of 4", "bomb of four"),
        StraightBomb("straight bomb", "street bomb");

        private final List<String> altNames;

        TemporaryPlayNamesEnum() {
            this.altNames = Collections.emptyList();
        }

        TemporaryPlayNamesEnum(String... altNames) {
            this.altNames = Arrays.asList(altNames);
        }

        public static TemporaryPlayNamesEnum byName(String name) {
            for (TemporaryPlayNamesEnum p : TemporaryPlayNamesEnum.values()) {
                if (name.equalsIgnoreCase(p.name())) {
                    return p;
                }
                if (p.altNames.contains(name.toLowerCase())) {
                    return p;
                }
            }
            return null;
        }
    }
}
