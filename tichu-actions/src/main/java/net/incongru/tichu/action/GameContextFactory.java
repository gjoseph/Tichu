package net.incongru.tichu.action;

@FunctionalInterface
public interface GameContextFactory<G extends GameContext> {

    G newContext();

}
