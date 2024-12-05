package net.incongru.tichu.simu.cmd;

import static java.util.function.Predicate.not;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.incongru.tichu.action.impl.PlayerPlaysResult;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Score;
import net.incongru.tichu.model.plays.Straight;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.util.NameableEnum;

public interface PostActionCommandFactory {
    Simulation.PostActionCommand expectSuccess();

    Simulation.PostActionCommand expectPlayResult(
        PostActionCommandFactory.ExpectablePlayResult expectedPlayResult
    );

    Simulation.PostActionCommand expectError(String expectedError);

    Simulation.PostActionCommand expectGameState(
        ExpectableGameState expectedGameState
    );

    Simulation.PostActionCommand expectPlay(ExpectablePlay play);

    Simulation.PostActionCommand expectWinTrick(String expectedPlayerName);

    Simulation.PostActionCommand expectNextPlayerToBe(
        String expectedPlayerName
    );

    Simulation.PostActionCommand expectEndOfRound();

    Simulation.PostActionCommand expectRoundScore(Score expectedScore);

    Simulation.PostActionCommand expectTotalScore(Score expectedScore);

    Simulation.PostActionCommand debugPlayerHand(String playerName);

    enum ExpectablePlay implements NameableEnum {
        Single,
        Pair,
        Triple("triplet"),
        FullHouse("full house"),
        Straight("street"),
        ConsecutivePairs("consecutive pairs"),
        BombOf4("bomb of 4", "bomb of four"),
        StraightBomb(
            play -> play instanceof Straight && play.isBomb(),
            "straight bomb",
            "street bomb"
        );

        private Predicate<Play> predicate;
        private final List<String> altNames;

        ExpectablePlay(String... altNames) {
            this(null, altNames);
        }

        ExpectablePlay(Predicate<Play> predicate, String... altNames) {
            final String name = name();
            this.predicate = predicate != null
                ? predicate
                : new ClassNamePredicate();
            this.altNames = Arrays.asList(altNames);
        }

        public boolean test(Play modelPlay) {
            return predicate.test(modelPlay);
        }

        @Override
        public List<String> altNames() {
            return altNames;
        }

        // I'm writing all this stuff purely because I don't want to copy class names into the constructors of the enum lol
        private class ClassNamePredicate implements Predicate<Play> {

            private Class clazz;

            public ClassNamePredicate() {
                final String className =
                    "net.incongru.tichu.model.plays." +
                    ExpectablePlay.this.name();
                try {
                    this.clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public boolean test(Play play) {
                return clazz.isInstance(play);
            }
        }
    }

    enum ExpectablePlayResult implements NameableEnum {
        NextGoes(
            Play.PlayResult.Result.NEXTGOES,
            PlayerPlaysResult.NEXT_PLAYER_GOES,
            "next goes",
            "next"
        ),
        TrickEnd(
            Play.PlayResult.Result.TRICK_END,
            PlayerPlaysResult.TRICK_END,
            "trick end",
            "take pile",
            "win trick"
        ), // TODO is this really win-trick?
        NotInHand(
            Play.PlayResult.Result.NOTINHAND,
            PlayerPlaysResult.NOT_IN_HAND,
            "not in hand",
            "cheat",
            "stop stealing cards you dingo"
        ),
        TooWeak(
            Play.PlayResult.Result.TOOWEAK,
            PlayerPlaysResult.TOO_WEAK,
            "too weak"
        ),
        InvalidPlay(
            Play.PlayResult.Result.INVALIDPLAY,
            PlayerPlaysResult.INVALID_PLAY,
            "invalid play",
            "invalid combo",
            "wtf is this even"
        ),
        NotYourTurn(
            Play.PlayResult.Result.INVALIDSTATE,
            PlayerPlaysResult.INVALID_STATE,
            "not your turn"
        );

        private final Play.PlayResult.Result modelEquivalent;
        private final PlayerPlaysResult actionResponseEquivalent;
        private final List<String> altNames;

        ExpectablePlayResult(
            Play.PlayResult.Result modelEquivalent,
            PlayerPlaysResult actionResponseEquivalent,
            String... altNames
        ) {
            this.modelEquivalent = modelEquivalent;
            this.actionResponseEquivalent = actionResponseEquivalent;
            this.altNames = Arrays.asList(altNames);
        }

        public boolean test(Play.PlayResult.Result modelResult) {
            return modelEquivalent == modelResult;
        }

        public boolean test(PlayerPlaysResult responseResult) {
            return actionResponseEquivalent == responseResult;
        }

        @Override
        public List<String> altNames() {
            return altNames;
        }
    }

    enum ExpectableGameState implements NameableEnum, Predicate<Game> {
        STARTED(Game::isStarted),
        NOT_STARTED(not(STARTED.predicate), "not started"),
        READY_TO_START(Game::isReadyToStart, "ready", "ready to start"),
        NOT_READY_TO_START(
            not(READY_TO_START.predicate),
            "not ready",
            "not ready to start"
        ),
        DONE(
            game -> {
                throw new IllegalStateException("Not implemented yet");
            },
            "done",
            "over",
            "end",
            "ended"
        ),
        NOT_DONE(
            not(READY_TO_START.predicate),
            "not done",
            "not over",
            "not end",
            "not ended"
        );

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
