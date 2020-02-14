package net.incongru.tichu.simu;

import net.incongru.tichu.model.Game;

public class SimulatedGameContext {
    private final Object lock = new Object();
    //    private List<Card> controlledDeck;
    private Game game;

//    public List<Card> controlledDeck() {
//        return this.controlledDeck;
//    }
//
//    public void controlledDeck(List<Card> controlledDeck) {
//        this.controlledDeck = controlledDeck;
//    }

    public void newGame(Game game) {
        synchronized (this.lock) {
            if (this.game != null) {
                throw new IllegalStateException("GameContext has already been initialised");
            }
            this.game = game;
        }
    }

    public Game game() {
        return game;
    }
}
