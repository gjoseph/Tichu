package net.incongru.tichu.simu.parse;

import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

import java.util.Arrays;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

class PACLineParsers extends AbstractLineParsers<Simulation.PostActionCommand> {

    PACLineParsers(PostActionCommandFactory pacFactory) {
        super("post-action-command", Arrays.asList(
                simpleParser(
                        t -> t.peek(0).equals("expect") && t.test(1, "error"),
                        t -> {
                            final String expectedError = t.remainder();
                            return pacFactory.expectError(expectedError);
                        }),
                simpleParser(
                        t -> t.peek(0).equals("expect") && t.test(1, "invalid-play"),
                        t -> {
                            final String expectedError = t.remainder();
                            return pacFactory.expectInvalidPlay(expectedError);
                        })
        ));
    }

}
