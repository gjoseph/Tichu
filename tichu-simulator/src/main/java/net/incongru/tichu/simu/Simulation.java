package net.incongru.tichu.simu;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
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
        ActionParam.WithActor actionParam();

        List<PostActionCommand> commands();
    }

    interface PostActionCommand {
        /**
         * @throws PostActionCommandException
         */
        void exec(SimulatedGameContext ctx, ActionResponse response);
    }

    class PostActionCommandException extends RuntimeException {
        public PostActionCommandException(String format, Object... args) {
            super(String.format(format, args));
        }
    }
}
