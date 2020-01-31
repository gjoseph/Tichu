package net.incongru.tichu.simu;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

class ActionLineParsers {
    private final List<ActionLineParser> parsers;

    public ActionLineParsers(ActionFactory actionFactory) {
        this.parsers = Arrays.asList(
                simpleParser(
                        t -> t.test(0, "init"),
                        t -> actionFactory.init())

        );
    }

    Action parse(String line) {
        final TokenisedLine tokens = new TokenisedLine(line);
        return parsers.stream()
                .filter(p -> p.accept(tokens))
                .findFirst()
                .map(p -> p.parse(tokens))
                .orElseThrow(() -> new LineParserException(tokens));
    }

    private ActionLineParser simpleParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, Action> function) {
        return new FunctionsBasedActionLineParser(predicate, function);
    }

    interface ActionLineParser {
        boolean accept(TokenisedLine t);

        Action parse(TokenisedLine t);
    }

    private static class FunctionsBasedActionLineParser implements ActionLineParser {
        private final Predicate<TokenisedLine> predicate;
        private final Function<TokenisedLine, Action> function;

        private FunctionsBasedActionLineParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, Action> function) {
            this.predicate = predicate;
            this.function = function;
        }

        public boolean accept(TokenisedLine t) {
            return predicate.test(t);
        }

        public Action parse(TokenisedLine t) {
            return function.apply(t);
        }

    }

    static class LineParserException extends RuntimeException {
        private LineParserException(TokenisedLine line) {
            super("Can't parse line [" + line.whole() + "], unrecognised action."); // TODO add possible actions, location and filename
        }
    }
}
