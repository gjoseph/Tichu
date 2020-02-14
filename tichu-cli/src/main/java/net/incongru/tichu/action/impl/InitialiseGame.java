package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Team;
import net.incongru.tichu.model.TichuRules;
import net.incongru.tichu.simu.SimulatedGameContext;

import java.util.List;
import java.util.Set;

// TODO this is for simulated/fake games. We probably need a different impl for real games.
// and/or SimulationContext is just a Provider<Game> and we can pass it along the exec() calls
class InitialiseGame implements Action {

    InitialiseGame() {
    }

    @Override
    public Action.Result exec(SimulatedGameContext ctx) {
        final Players players = new Players();
        players.add(new Team("Team 1"));
        players.add(new Team("Team 2"));

        final TichuRules rules = new SimulatedTichuRules();

        final Game game = new Game(players, rules);
        ctx.newGame(game);
        return new Success() {
            @Override
            public String publicLog() {
                return "Game initialised";
            }

            @Override
            public String actorLog() {
                return "Game initialised";
            }
        };
    }

    static class SimulatedTichuRules extends TichuRules {
        private List<Card> controlledDeck;

        @Override
        public CardDeck newShuffledDeck() {
            return new CardDeck() {
                @Override
                protected List<Card> shuffle(Set<Card> cards) {
                    // Returned the controlled ordered list instead.
                    return controlledDeck();
                }
            };
        }

        public List<Card> controlledDeck() {
            return this.controlledDeck;
        }

        public void controlledDeck(List<Card> controlledDeck) {
            this.controlledDeck = controlledDeck;
        }

    }
}
