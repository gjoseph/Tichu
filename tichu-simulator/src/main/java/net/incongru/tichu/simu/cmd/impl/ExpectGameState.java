package net.incongru.tichu.simu.cmd.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.SimulatedGameContext;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

class ExpectGameState implements Simulation.PostActionCommand {
    private final PostActionCommandFactory.ExpectableGameState expectedGameState;

    ExpectGameState(PostActionCommandFactory.ExpectableGameState expectedGameState) {
        this.expectedGameState = expectedGameState;
    }

    @Override
    public void exec(SimulatedGameContext ctx, Action.Result result) {
        final boolean match = expectedGameState.test(ctx.game());
        if (match) {
            ctx.log("Game is %s, as expected", expectedGameState);
        } else {
            throw new Simulation.PostActionCommandException("Game was expected to be %s at this point", expectedGameState);
        }
    }
}
