package net.incongru.tichu.simu.parse;

import net.incongru.tichu.simu.Simulation;

import java.util.Arrays;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

class CommandLineParsers extends AbstractLineParsers<Simulation.Command> {
    CommandLineParsers() {
        super("command", Arrays.asList(
                simpleParser(
                        t -> t.peek(0).equals("expect") && t.test(1, "error"),
                        t -> {
                            final String expectedError = t.remainder();
                            return new Simulation.Command() {
                                @Override
                                public String toString() {
                                    return "expect:error:" + expectedError;
                                }
                            };
                        })
        ));

    }
}
