package net.incongru.tichu.model;

/**
 * Models most (all?) rules of the game, such that, maybe, one day (...) we can make this an interface and swap out some rules. Whatever.
 */
public class TichuRules {
    public boolean isBomb(Trick.Play play) {
        throw new IllegalStateException("not implemented yet"); // TODO
    }
}
