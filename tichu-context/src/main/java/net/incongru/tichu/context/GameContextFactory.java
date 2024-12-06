package net.incongru.tichu.context;

@FunctionalInterface
public interface GameContextFactory<G extends GameContext> {
    G newContext();
}
