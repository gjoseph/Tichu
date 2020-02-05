package net.incongru.tichu.simu;


import net.incongru.tichu.action.Action;
import net.incongru.tichu.simu.parse.SimulationFileParser;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Executes a game's actions and tests, represented in a text file.
 */
public class TichuSimulator {
    public TichuSimulator() {
    }

    void executeSimulation(Path p) throws IOException {
        final Simulation simu = new SimulationFileParser().parse(p);
        for (Simulation.ActionAndCommands actionAndCommands : simu.actionAndCommands()) {
            final Action.Result res = actionAndCommands.action().exec();
            for (Simulation.PostActionCommand postActionCommand : actionAndCommands.commands()) {
                postActionCommand.exec(res);
            }
        }
    }

}
