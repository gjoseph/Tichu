package net.incongru.tichu.action;

/**
 * @param <P> the type of {@link ActionParam} this action supports.
 * @see ActionFactory
 */
public interface Action<P extends ActionParam, R extends ActionResponse.Result> {

    // TODO add a description here or on param type

    ActionResponse<R> exec(GameContext ctx, ActionParam.WithActor<P> actionParam);

    enum ActionType {
        INIT, JOIN, READY,
        CHEAT_DEAL,
        NEW_TRICK, // not sure we need this as an action to begin with
        PLAY, PASS
    }

}
