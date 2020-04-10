package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
import net.incongru.tichu.action.ActionResult.Success;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Round;
import net.incongru.tichu.model.Team;
import net.incongru.tichu.model.TichuRules;

import java.util.List;
import java.util.Set;

// TODO this is for simulated/fake games. We probably need a different impl for real games.
// and/or SimulationContext is just a Provider<Game> and we can pass it along the exec() calls
class InitialiseGame implements Action<InitialiseGameParam> {

    InitialiseGame() {
    }

    @Override
    public ActionResult exec(GameContext ctx, ActionParam.WithActor<InitialiseGameParam> param) {
        final Players players = new Players();
        players.add(new Team("Team 1"));
        players.add(new Team("Team 2"));

        final TichuRules rules = new SimulatedTichuRules();

        final Game game = new Game(players, rules) {
            @Override
            protected Round newRound() {
                return new Round(this) {
                    @Override
                    protected void shuffleAndDeal() {
                        // noop, cards should have been dealt via CheatDeal actions
                    }
                };
            }
        };
        ctx.newGame(game);
        return new Success() {
//                return "Game initialised";
        };
    }

    static class SimulatedTichuRules extends TichuRules {
        private List<Card> controlledDeck;

        // TODO do we actually need this, since we dont... use the deck to deal?
        // ... but we do.. through net.incongru.tichu.model.Round.shuffleAndDeal
        // maybe what we don't need is #controlledDesk
        @Override
        public CardDeck newShuffledDeck() {
            return new CardDeck() {
                @Override
                protected List<Card> shuffle(Set<Card> cards) {
                    // Returned the controlled ordered list instead.
                    return controlledDeck();
                }

                @Override
                public Card take() {
                    return super.take();
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
