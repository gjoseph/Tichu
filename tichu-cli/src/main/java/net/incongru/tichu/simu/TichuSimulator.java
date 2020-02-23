package net.incongru.tichu.simu;


import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.parse.SimulationFileParser;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Executes a game's actions and tests, represented in a text file.
 */
public class TichuSimulator {

    private final GameContextFactory gameContextFactory;

    public TichuSimulator() {
        this.gameContextFactory = SimulatedGameContext::new;
    }

    void executeSimulation(Path p) throws IOException {
        final Simulation simu = new SimulationFileParser().parse(p);
        final SimulatedGameContext ctx = gameContextFactory.newContext();

        for (Simulation.ActionAndCommands actionAndCommands : simu.actionAndCommands()) {
            System.out.println("Executing action: " + actionAndCommands.action());
            final Action.Result res = actionAndCommands.action().exec(ctx);
            System.out.println("Result: " + res);
            for (Simulation.PostActionCommand postActionCommand : actionAndCommands.commands()) {
                System.out.println("PostActionCommand: " + postActionCommand);
                postActionCommand.exec(ctx, res);
            }
            System.out.println();
        }
    }

}
