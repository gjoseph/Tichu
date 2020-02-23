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

        List<PostActionCommand> commands();
    }

    interface PostActionCommand {
        /**
         * @throws PostActionCommandException
         */
        void exec(SimulatedGameContext ctx, Action.Result result);
    }

    class PostActionCommandException extends RuntimeException {
        public PostActionCommandException(String format, Object... args) {
            super(String.format(format, args));
        }
    }
}
