package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.model.Score;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

public class PostActionCommandFactoryImpl implements PostActionCommandFactory {

    @Override
    public Simulation.PostActionCommand expectSuccess() {
        return new ExpectSuccess();
    }

    @Override
    public Simulation.PostActionCommand expectPlayResult(
        PostActionCommandFactory.ExpectablePlayResult expectedPlayResult
    ) {
        return new ExpectPlayResult(expectedPlayResult);
    }

    @Override
    public Simulation.PostActionCommand expectError(String expectedError) {
        return new ExpectError(expectedError);
    }

    @Override
    public Simulation.PostActionCommand expectGameState(
        ExpectableGameState expectedGameState
    ) {
        return new ExpectGameState(expectedGameState);
    }

    @Override
    public Simulation.PostActionCommand expectPlay(ExpectablePlay play) {
        return new ExpectPlay(play);
    }

    @Override
    public Simulation.PostActionCommand expectWinTrick(
        String expectedPlayerName
    ) {
        return new ExpectWinTrick(UserId.of(expectedPlayerName));
    }

    @Override
    public Simulation.PostActionCommand expectNextPlayerToBe(
        String expectedPlayerName
    ) {
        return new ExpectNextPlayerToBe(UserId.of(expectedPlayerName));
    }

    @Override
    public Simulation.PostActionCommand expectEndOfRound() {
        return new ExpectEndOfRound();
    }

    @Override
    public Simulation.PostActionCommand expectRoundScore(Score expectedScore) {
        return new ExpectRoundScore(expectedScore);
    }

    @Override
    public Simulation.PostActionCommand expectTotalScore(Score expectedScore) {
        return new ExpectTotalScore(expectedScore);
    }

    @Override
    public Simulation.PostActionCommand debugPlayerHand(String playerName) {
        return new DebugPlayerHand(UserId.of(playerName));
    }
}
