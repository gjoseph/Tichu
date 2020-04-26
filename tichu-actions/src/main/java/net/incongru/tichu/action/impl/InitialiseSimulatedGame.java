package net.incongru.tichu.action.impl;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Round;
import net.incongru.tichu.model.TichuRules;

import java.util.List;
import java.util.Set;

/**
 * This initialises a simulated game, where cards aren't shuffled or dealt but rather explicitly distributed by another
 * action.
 */
class InitialiseSimulatedGame extends InitialiseGame {

    InitialiseSimulatedGame() {
    }

    @Override
    protected Game newGame(Players players) {
        final TichuRules rules = new SimulatedTichuRules();

        return new Game(players, rules) {
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
