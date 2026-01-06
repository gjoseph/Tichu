package net.incongru.tichu.simu;

import net.incongru.tichu.action.AbstractGameContext;

public class SimulatedGameContext extends AbstractGameContext {

    private final boolean consoleLog;

    /**
     * @param consoleLog Set to true for good ole console debugging in tests -- meanwhile, shush!
     */
    public SimulatedGameContext(boolean consoleLog) {
        this.consoleLog = consoleLog;
    }

    /**
     * Logs a message to the simulation.
     *
     * @see String#formatted(Object...)
     */
    @Override
    public void log(String msg, Object... args) {
        if (consoleLog) {
            System.out.println(msg.formatted(args));
        }
    }

    @Override
    public void debug(String msg, Object... args) {
        this.log("[debug] " + msg, args);
    }
}
