package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.NewTrickParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static net.incongru.tichu.simu.parse.FunctionsBasedLineParser.simpleParser;

class ActionLineParsers extends AbstractLineParsers<ActionParam.WithActor> {

    ActionLineParsers() {
        super("action", Arrays.asList(
                simpleParser(
                        t -> t.test(0, "init"),
                        t -> InitialiseGameParam.withActor(UserId.of("dummy"))), // TODO player id ?
                simpleParser(
                        t -> t.test(1, "joins") &&
                                t.test(1, "team"), // or "table"
                        t -> {
                            final String playerName = t.pop(0);
                            final int team = t.popInt(0) - 1; // team is 0-indexed, but we expect the text interface to be 1-indexed
                            return JoinTableParam.withActor(UserId.of(playerName), team);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "is") && t.test(1, "ready"),
                        t -> {
                            final String playerName = t.pop(0);
                            return PlayerIsReadyParam.withActor(UserId.of(playerName));
                        }
                ),
                simpleParser(
                        t -> t.test(0, "cheat-deal"),
                        t -> {
                            final String playerName = t.pop(0);
                            final Set<Card> cards = t.remainderAsCards();
                            return CheatDealParam.withActor(UserId.of(playerName), cards);
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
                            return PlayerPlaysParam.withActor(UserId.of(playerName), cards);
                        }
                ),
                simpleParser(
                        t -> t.test(1, "passes"),
                        t -> {
                            final String playerName = t.pop(0);
                            return PlayerPlaysParam.withActor(UserId.of(playerName), Collections.emptySet());
                        }
                ),
                simpleParser(
                        t -> t.test(0, "new") && t.test(0, "trick"),
                        t -> NewTrickParam.withActor(UserId.of("dummy")) // TODO player id ?
                )
        ));
    }

}
