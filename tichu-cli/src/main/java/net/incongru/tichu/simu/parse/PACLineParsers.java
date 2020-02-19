package net.incongru.tichu.simu.parse;

import net.incongru.tichu.simu.Simulation;
import net.incongru.tichu.simu.cmd.PostActionCommandFactory;

import java.util.Arrays;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

/**
 * Parsers for PostActionCommands; the leading `-` for PACs is expected to have been removed prior to calling these.
 */
class PACLineParsers extends AbstractLineParsers<Simulation.PostActionCommand> {

    PACLineParsers(PostActionCommandFactory pacFactory) {
        super("post-action-command", Arrays.asList(
                simpleParser(
                        t -> expect(t, "success"),
                        t -> pacFactory.expectSuccess()
                ),
                simpleParser(
                        t -> expect(t, "error"),
                        t -> pacFactory.expectError(t.remainder())
                ),
                simpleParser(
                        t -> expect(t, "invalid-play"),
                        t -> pacFactory.expectInvalidPlay(t.remainder())
                ),
                simpleParser(
                        t -> expect(t, "played"),
                        t -> {
                            final PostActionCommandFactory.TemporaryPlayNamesEnum play = PostActionCommandFactory.TemporaryPlayNamesEnum.byName(t.remainder());
                            return pacFactory.expectPlay(play);
                        }
                ),
                simpleParser(
                        t -> expect(t, "win-trick"),
                        t -> pacFactory.expectWinTrick()
                ),
                simpleParser(
                        t -> t.test(2, "round") && expect(t, "end"),
                        t -> pacFactory.expectEndOfRound()
                ),
                simpleParser(
                        t -> expect(t, "score"),
                        t -> pacFactory.expectRoundScore(t.pop(0), t.popInt(0))
                ),
                simpleParser(
                        t -> t.peek(0, "debug") && t.test(2, "hand"),
                        t -> pacFactory.debugPlayerHand(t.pop(1))
                )
        ));
    }

    private static boolean expect(TokenisedLine t, String s) {
        return t.test(1, s) && t.test(0, "expect");
    }

}
