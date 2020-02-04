package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.impl.ActionFactoryImpl;
import net.incongru.tichu.simu.ImmutableActionAndCommands;
import net.incongru.tichu.simu.ImmutableSimulation;
import net.incongru.tichu.simu.Simulation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SimulationFileParser {

    private final ActionLineParsers actionLineParsers;
    private final CommandLineParsers commandLineParsers;

    public SimulationFileParser() {
        actionLineParsers = new ActionLineParsers(new ActionFactoryImpl());
        commandLineParsers = new CommandLineParsers();
    }

    Simulation parse(Path p) throws IOException {
        final ImmutableSimulation.Builder builder = ImmutableSimulation.builder();
        final List<String> lines = SimulationFileLoader.from(p);
        int i = 0;
        while (i < lines.size()) {
            final TokenisedLine tokens = new TokenisedLine(lines.get(i));
            final Action action = actionLineParsers.parse(tokens);
            final ImmutableActionAndCommands.Builder ae = ImmutableActionAndCommands.builder().action(action);
            while (nextLineIsExpectation(lines, i)) {
                i++;
                final TokenisedLine cmdTokens = new TokenisedLine(lines.get(i));
                cmdTokens.pop(0); // strip leading dash -- TODO this whole loop could be improved, but needs test before refactor
                final Simulation.Command cmd = commandLineParsers.parse(cmdTokens);
                ae.addCommands(cmd);
            }
            final ImmutableActionAndCommands build = ae.build();
            builder.addActionAndCommands(build);
            i++;
        }
        return builder.build();
    }

    private boolean nextLineIsExpectation(List<String> lines, int i) {
        // YUCK
        return i < lines.size() - 1 && lines.get(i + 1).startsWith("-");
    }
}
