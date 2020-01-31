package net.incongru.tichu.simu;

import net.incongru.tichu.action.Action;
import org.immutables.value.Value;

import java.util.List;

/**
 * Describes the simulation.
 */
@Value.Immutable
public interface Simulation {
    List<ActionAndExpectations> actionAndExpectations();

    @Value.Immutable
    interface ActionAndExpectations {
        Action action();

        List<Expectation> expectations();
    }

    interface Expectation {
    }
}
