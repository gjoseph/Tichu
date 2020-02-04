package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.model.Card;

import java.util.Arrays;
import java.util.List;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

class ActionLineParsers {
    private final List<LineParser<Action>> actionParsers;

    public ActionLineParsers(ActionFactory actionFactory) {
        this.actionParsers = Arrays.asList(
                simpleParser(
                        t -> t.test(0, "init"),
                        t -> actionFactory.init()),
                simpleParser(
                        t -> t.test(1, "joins") &&
                                t.test(1, "team"),
                        t -> {
                            final String playerName = t.pop(0);
                            final int team = t.popInt(0) - 1;
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
                            final List<Card> cards = t.remainderAsCards();
                            return actionFactory.cheatDeal(playerName, cards);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "plays"),
                        t -> {
                            final String playerName = t.pop(0);
                            final List<Card> cards = t.remainderAsCards();
                            return actionFactory.plays(playerName, cards);
                        }
                )

        );
    }

    Action parse(String line) {
        final TokenisedLine tokens = new TokenisedLine(line);
        return actionParsers.stream()
                .filter(p -> p.accept(tokens))
                .findFirst()
                .map(p -> p.parse(tokens))
                .orElseThrow(() -> new LineParserException(tokens, "unrecognised action"));
    }

}
