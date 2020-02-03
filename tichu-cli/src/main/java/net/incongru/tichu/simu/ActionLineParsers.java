package net.incongru.tichu.simu;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ActionLineParsers {
    private final List<ActionLineParser> parsers;

    public ActionLineParsers(ActionFactory actionFactory) {
        this.parsers = Arrays.asList(
                simpleParser(
                        t -> t.test(0, "init"),
                        t -> actionFactory.init()),
                simpleParser(
                        t -> t.test(1, "joins") &&
                                t.test(1, "team"),
                        t -> {
                            final String playerName = t.pop(0);
                            final int team = popInt(t, 0) - 1;
                            return actionFactory.joinTeam(playerName, team);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "is") && t.test(1, "ready"), // TODO test for known player names?
                        t -> {
                            final String playerName = t.pop(0);
                            return actionFactory.isReady(playerName);
                        }
                ),
                simpleParser(
                        t -> t.test(0, "cheat-deal"),
                        t -> {
                            final String playerName = t.pop(0);
                            final String cardsStr = t.remainder();
                            final List<Card> cards = Arrays.stream(cardsStr.split("\\s*,\\s*"))
                                    .map(DeckConstants::byName)
                                    .collect(Collectors.toList());
                            return actionFactory.cheatDeal(playerName, cards);

                        }
                )

        );
    }

    Action parse(String line) {
        final TokenisedLine tokens = new TokenisedLine(line);
        return parsers.stream()
                .filter(p -> p.accept(tokens))
                .findFirst()
                .map(p -> p.parse(tokens))
                .orElseThrow(() -> new LineParserException(tokens, "unrecognised action"));
    }

    private int popInt(TokenisedLine t, int i) {
        try {
            return Integer.parseInt(t.pop(i));
        } catch (NumberFormatException e) {
            throw new LineParserException(t, e.getMessage());
        }
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
        private LineParserException(TokenisedLine line, String message) {
            super("Can't parse line [" + line.whole() + "], " + message + "."); // TODO add possible actions, location and filename
        }
    }
}
