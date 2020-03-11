package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.simu.ImmutableActionAndCommands;
import net.incongru.tichu.simu.ImmutableSimulation;
import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.impl.PostActionCommandFactoryImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SimulationFileParser {

    private final ActionLineParsers actionLineParsers;
    private final PACLineParsers pacLineParsers;
    private final PostActionCommandFactoryImpl pacFactory;

    public SimulationFileParser() {
        actionLineParsers = new ActionLineParsers();
        pacFactory = new PostActionCommandFactoryImpl();
        pacLineParsers = new PACLineParsers(pacFactory);
    }

    public Simulation parse(Path p) throws IOException {
        final ImmutableSimulation.Builder builder = ImmutableSimulation.builder();
        final List<String> lines = SimulationFileLoader.from(p);
        int i = 0;
        while (i < lines.size()) {
            final TokenisedLine tokens = new TokenisedLine(lines.get(i));
            final ActionParam param = actionLineParsers.parse(tokens);
            final ImmutableActionAndCommands.Builder actionAndCommandsBuilder = ImmutableActionAndCommands.builder().actionParam(param);
            while (nextLineIsExpectation(lines, i)) {
                i++;
                final TokenisedLine cmdTokens = new TokenisedLine(lines.get(i));
                cmdTokens.pop(0); // strip leading dash -- TODO this whole loop could be improved, but needs test before refactor
                final Simulation.PostActionCommand cmd = pacLineParsers.parse(cmdTokens);
                actionAndCommandsBuilder.addCommands(cmd);
            }

            final ImmutableActionAndCommands actionAndCommands = actionAndCommandsBuilder.build();
            if (actionAndCommands.commands().isEmpty()) {
                builder.addActionAndCommands(actionAndCommands.withCommands(defaultPostActionCommand()));
            } else {
                builder.addActionAndCommands(actionAndCommands);
            }
            i++;
        }
        return builder.build();
    }


    /**
     * When no post-action command is specifically specified by the simulation, execute the default one.
     */
    private Simulation.PostActionCommand defaultPostActionCommand() {
        return pacFactory.expectSuccess();

    }

    private boolean nextLineIsExpectation(List<String> lines, int i) {
        // YUCK
        return i < lines.size() - 1 && lines.get(i + 1).startsWith("-");
    }
}
