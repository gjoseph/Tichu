package net.incongru.tichu.simu;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResult;
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
        ActionParam actionParam();

        List<PostActionCommand> commands();
    }

    interface PostActionCommand {
        /**
         * @throws PostActionCommandException
         */
        void exec(SimulatedGameContext ctx, ActionResult result);
    }

    class PostActionCommandException extends RuntimeException {
        public PostActionCommandException(String format, Object... args) {
            super(String.format(format, args));
        }
    }
}
