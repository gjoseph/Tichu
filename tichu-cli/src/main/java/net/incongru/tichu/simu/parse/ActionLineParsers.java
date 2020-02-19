package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.model.Card;

import java.util.Arrays;
import java.util.Set;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

class ActionLineParsers extends AbstractLineParsers<Action> {

    ActionLineParsers(ActionFactory actionFactory) {
        super("action", Arrays.asList(
                simpleParser(
                        t -> t.test(0, "init"),
                        t -> actionFactory.init()),
                simpleParser(
                        t -> t.test(1, "joins") &&
                                t.test(1, "team"),
                        t -> {
                            final String playerName = t.pop(0);
                            final int team = t.popInt(0) - 1; // team is 0-indexed, but we expect the text interface to be 1-indexed
                            return actionFactory.joinTeam(playerName, team);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "is") && t.test(1, "ready"),
                        t -> {
                            final String playerName = t.pop(0);
                            return actionFactory.isReady(playerName);
                        }
                ),
                simpleParser(
                        t -> t.test(0, "cheat-deal"),
                        t -> {
                            final String playerName = t.pop(0);
                            final Set<Card> cards = t.remainderAsCards();
                            return actionFactory.cheatDeal(playerName, cards);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "plays"),
                        t -> {
                            final String playerName = t.pop(0);
                            if (t.count() < 1) {
                                throw new LineParserException(t, "No cards played, use the '<player> passes' action instead.");
                            }
                            final Set<Card> cards = t.remainderAsCards();
                            return actionFactory.plays(playerName, cards);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "passes"),
                        t -> {
                            final String playerName = t.pop(0);
                            return actionFactory.passes(playerName);
                        }
                ),
                simpleParser(
                        t -> t.test(0, "new") && t.test(0, "trick"),
                        t -> actionFactory.newTrick()
                )
        ));
    }

}
