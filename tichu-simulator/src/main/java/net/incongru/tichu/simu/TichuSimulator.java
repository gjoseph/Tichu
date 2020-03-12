package net.incongru.tichu.simu;


import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.impl.ActionFactoryImpl;
import net.incongru.tichu.simu.parse.SimulationFileParser;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Executes a game's actions and tests, represented in a text file.
 */
public class TichuSimulator {

    private final GameContextFactory gameContextFactory;
    private final ActionFactory actionFactory;

    public TichuSimulator() {
        this.gameContextFactory = SimulatedGameContext::new;
        this.actionFactory = new ActionFactoryImpl();
    }

    void executeSimulation(Path p) throws IOException {
        final Simulation simu = new SimulationFileParser().parse(p);
        final SimulatedGameContext ctx = gameContextFactory.newContext();

        for (Simulation.ActionAndCommands actionAndCommands : simu.actionAndCommands()) {
            final ActionParam actionParam = actionAndCommands.actionParam();
            System.out.println("Executing action: " + actionParam);
            final Action action = actionFactory.actionFor(actionParam);
            final ActionResult res = action.exec(ctx, actionParam);
            System.out.println("Result: " + res);
            for (Simulation.PostActionCommand postActionCommand : actionAndCommands.commands()) {
                System.out.println("PostActionCommand: " + postActionCommand);
                postActionCommand.exec(ctx, res);
            }
            System.out.println();
        }
    }

}
