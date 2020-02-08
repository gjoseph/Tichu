package net.incongru.tichu.cli.oldstuff;

import com.google.common.collect.Sets;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.TichuRules;
import net.incongru.tichu.model.Trick;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is the remains of a class that tried to extend Groovy's Script class, to try and provide a DSL.
 * Converted this from shit groovy code to shit java code, keeping this around until i get to rewrite and/or find a
 * better approach for a dsl/text parsing thing.
 *
 * Everything was meant to return closures, which were executed by TichuCli.
 * To be DSL-ish, it relied on Groovy's optional parenthesis. For methods with no params, parens are mandatory, so we cheat
 * and exposed them as bean properties (i.e 'getHelp()' is invoked with 'help')
 */
public class TichuDSL {
    private Game game;

    public TichuDSL() {
    }

    private Game game() {
        if (this.game == null) {
            throw new IllegalStateException();
        }
        return this.game;
    }

    public Supplier<Void> getHelp() {
        return () -> {
            // ui.println("Help text here");
            return null;
        };
    }

    public Supplier<Void> getQuit() {
        return () -> {
            System.exit(0);// Very eggelant
            return null;
        };
    }

    public Supplier<Void> getScore() {
        return () -> {
            // ui.println("> Current score: " + String.valueOf(game().globalScore()));
            return null;
        };
    }

    public Supplier<Void> getStart() {
        return () -> {
            // ui.println("> New game ...");
            final Players players = new Players();
            final Game game = new Game(players, new TichuRules());

            // TODO create game when players join/identify themselves, start it on demand too

            final Trick firstTrick = game.start().start();
            // ui.println("> " + firstTrick.nextPlayer().name() + " goes ...");
            this.game = game;
            return null;
        };
    }

    public Supplier<ActingPlayer> p(int i) {
        return player(i);
    }

    public Supplier<ActingPlayer> player(int i) {
        return () -> {
            Player player = game().players().getPlayer(i);
            return playerActions(player);
        };
    }

    public Supplier<ActingPlayer> player(final String name) {
        return () -> {
            Player player = game().players().getPlayerByName(name).orElseThrow(() -> new IllegalArgumentException());
            return playerActions(player);
        };
    }

    private ActingPlayer playerActions(Player p) {
        return new ActingPlayer(this, p);
    }

    // public Closure<Closure<Object>> plays( Player p) {
    // public Closure<Closure<Object>> plays( Card... cards) {

    public Function<Player, Void> playerPlayCards(final Card... cards) {
        return (Player p) -> {
//            if (!p.hand.containsAll(cards)) {
//                    a.fg(Ansi.Color.BLUE).a("> ${p.name()} tried to be a smart ass and played cards he/she doesn't have: ${cards-p.hand}")
//            }
            final Play.PlayResult res = game().currentRound().currentTrick().play(p, Sets.newHashSet(cards));
            // ui.println("> " + p.name() + " played " + String.valueOf(res.play()) + ": " + String.valueOf(res.result()) + " : " + res.message());
            // ui.printAnsi(a -> {
            //   return a.fg(Ansi.Color.WHITE).a(":: cards left in " + p.name() + "\'s hand: " + String.valueOf(p.hand()));
            // });
            return null;
        };
    }

    public Function<Player, ActingPlayer> plays() {
        return (Player p) -> {
            return new ActingPlayer(TichuDSL.this, p);
        };
    }

    private Function<Player, Void> passes() {
        return (Player p) -> {

            //TODO is it even their turn ?
            // this.ui.println("> " + p.name() + " passes.");
            return playerPlayCards(new Card[0]).apply(p);
        };
    }

    public Function<Player, Void> showHand() {
        return (Player p) -> {
            // ui.printAnsi(a -> a.fg(Ansi.Color.CYAN).a(p.name() + "'s hand: " + String.valueOf(p.hand())));
            return null;
        };
    }

}
