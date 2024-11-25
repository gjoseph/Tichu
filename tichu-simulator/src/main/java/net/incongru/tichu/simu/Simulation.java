package net.incongru.tichu.simu;

import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describes the simulation.
 */
public record Simulation(
        List<ActionAndCommands> actionAndCommands
) {
    public record ActionAndCommands(
            ActionParam.WithActor actionParam,
            List<PostActionCommand> commands
    ) {

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private ActionParam.WithActor actionParam;
            private List<PostActionCommand> commands = new ArrayList<>();

            public Builder actionParam(ActionParam.WithActor actionParam) {
                this.actionParam = actionParam;
                return this;
            }

            public Builder addCommand(PostActionCommand command) {
                this.commands.add(command);
                return this;
            }

            public ActionAndCommands buildWithDefaultCommand(PostActionCommand defaultCommand) {
                if (commands.isEmpty()) {
                    return new ActionAndCommands(actionParam, Collections.singletonList(defaultCommand));
                } else {
                    return new ActionAndCommands(actionParam, commands);
                }
            }
        }
    }

    public interface PostActionCommand {
        void exec(SimulatedGameContext ctx, ActionResponse response) throws PostActionCommandException;
    }

    public static class PostActionCommandException extends RuntimeException {
        public PostActionCommandException(String format, Object... args) {
            super(String.format(format, args));
        }
    }
}