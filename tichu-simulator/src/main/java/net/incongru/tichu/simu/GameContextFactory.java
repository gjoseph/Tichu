package net.incongru.tichu.simu;

@FunctionalInterface
public interface GameContextFactory {

    SimulatedGameContext newContext();

}
