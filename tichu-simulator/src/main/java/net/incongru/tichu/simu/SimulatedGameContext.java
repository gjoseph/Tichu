package net.incongru.tichu.simu;

import net.incongru.tichu.action.AbstractGameContext;

public class SimulatedGameContext extends AbstractGameContext {

    /**
     * Logs a message to the simulation.
     *
     * @see String#formatted(Object...)
     */
    @Override
    public void log(String msg, Object... args) {
        System.out.println(msg.formatted(args));
    }
}
