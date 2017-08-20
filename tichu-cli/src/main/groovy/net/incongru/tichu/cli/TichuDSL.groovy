package net.incongru.tichu.cli

import net.incongru.tichu.model.Card
import net.incongru.tichu.model.Game
import net.incongru.tichu.model.Play
import net.incongru.tichu.model.Players
import net.incongru.tichu.model.Players.Player
import net.incongru.tichu.model.TichuRules
import net.incongru.tichu.model.Trick
import org.codehaus.groovy.control.CompilationFailedException
import org.fusesource.jansi.Ansi

/**
 * Everything here returns closures, which are executed by TichuCli.
 * To be DSL-ish, we rely on Groovy's optional parenthesis. For methods with no params, parens are mandatory, so we cheat
 * and expose them as bean properties (i.e 'getHelp()' is invoked with 'help')
 */
abstract class TichuDSL extends Script {

    TichuDSL() {
        super()
    }

    // if private, these methods can't be called from script "body"
    UI ui() {
        getBinding().getVariable("ui") as UI
    }

    Game game() {
        if (!getBinding().hasVariable("game")) {
            throw new IllegalStateException("No game in progress");
        }
        getBinding().getVariable("game") as Game
    }

    def getHelp() {
        return {
            ui().println """Help text here"""
        }
    }

    def getQuit() {
        return {
            System.exit(0) // Very eggelant
        }
    }

    def getScore() {
        return {
            ui().println "> Current score: ${game().globalScore()}"
        }
    }

    def getStart() {
        return {
            ui().println "> New game ..."
            final Players players = new Players("Greg", "Rufus", "Team Gruf", "Isa", "Kade", "Team Kasa");
            final Game game = new Game(players, new TichuRules());

            // TODO create game when players join/identify themselves, start it on demand too

            Trick firstTrick = game.start().start();
            ui().println "> ${firstTrick.nextPlayer().name()} goes ..."
            getBinding().setVariable("game", game)
        }
    }

    // Synonym for player()
    def p(int i) { player(i) }

    def player(int i) {
        playerActions(game().players().getPlayer(i))
    }

    def player(String name) {
        def player = game().players().getPlayerByName(name).orElseThrow({
            // TODO handle more gracefully
            new IllegalArgumentException("No player called ${name}")
        })

        playerActions(player)
    }

    def playerActions(Players.Player p) {
        return new ActingPlayer(this, p)
    }

    public def plays = { Player p ->
        new ActingPlayer(this, p)
    }

    def plays(Players.Player p) {
        return { Card... cards ->
            return playerPlayCards(p, cards)
        }
    }

    def plays(Card... cards) {
        return { Players.Player p ->
            return playerPlayCards(p, cards)
        }
    }

    def playerPlayCards(Players.Player p, Card... cards) {
        return {
            // moved this into model, but we need better message handling
//            if (!p.hand.containsAll(cards)) {
//                    a.fg(Ansi.Color.BLUE).a("> ${p.name()} tried to be a smart ass and played cards he/she doesn't have: ${cards-p.hand}")
//            }


            Play.PlayResult res = game().currentRound().currentTrick().play(p, cards.toList().toSet());
            ui().println "> ${p.name()} played ${res.play}: ${res.result} : ${res.message}"
            ui().printAnsi { Ansi a ->
                a.fg(Ansi.Color.WHITE).a(":: cards left in ${p.name()}'s hand: ${p.hand()}")
            }
        }
    }

    // There work in the "<player_name> <action>" syntax
    def passes = { Player p ->
        //TODO is it even their turn ?
        return {
            playerPlayCards(p)()
            ui().println "> ${p.name()} passes."
        }
    }

    public def hand = { Player p ->
        return {
            ui().printAnsi(new UI.WithAnsi() {
                @Override
                Ansi doIt(Ansi a) {
                    a.fg(Ansi.Color.CYAN).a("${p.name()}'s hand: ${p.hand().sort(false, Card.Comparators.BY_PLAY_ORDER)}")
                }
            })
        }
    }

    @Override
    Object evaluate(String expression) throws CompilationFailedException {
        return super.evaluate(expression)
    }

    @Override
    Object invokeMethod(String name, Object args) {
        // player-name plays <cards> -> playerName(closureOfPlay(cards))
        Optional activity = game().players().getPlayerByName(name).map { p ->
            Closure playerActivity = args[0]
            playerActivity.call(p)
        }
        return activity.orElseGet({ super.invokeMethod(name, args) })
    }

    @Override
    def getProperty(String property) {
        // Ugh, if we try to println for debugging here, this tries to get the "out" property -> stack overflow println "property = $property"
        Optional<Object> card = DeckConstants.byName(property)
//        return card.orElseGet({ super.getProperty(property) })
        Optional<Object> player = getBinding().hasVariable("game") ?
                game().players().getPlayerByName(property).map { p ->
                    //println("Found player $property, return actions: ${playerActions(p)}")
                    playerActions(p)
                } : Optional.empty()

        return card.orElseGet({ player.orElseGet({ super.getProperty(property) }) })
    }
}