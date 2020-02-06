package net.incongru.tichu.simu;

@FunctionalInterface
public interface GameContextFactory {

    GameContext newContext();

}
