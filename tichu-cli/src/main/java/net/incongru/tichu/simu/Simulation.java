package net.incongru.tichu.simu;

import net.incongru.tichu.action.Action;
import org.immutables.value.Value;

import java.util.List;

/**
 * Describes the simulation.
 */
@Value.Immutable
public interface Simulation {
    List<ActionAndCommands> actionAndCommands();

    @Value.Immutable
    interface ActionAndCommands {
        Action action();

        List<Command> commands();
    }

    interface Command {
    }
}
