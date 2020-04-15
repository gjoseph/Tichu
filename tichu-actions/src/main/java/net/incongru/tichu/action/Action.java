package net.incongru.tichu.action;

/**
 * @param <P> the type of {@link ActionParam} this action supports.
 * @see ActionFactory
 */
public interface Action<P extends ActionParam> {

    // TODO add a description here or on param type

    ActionResponse exec(GameContext ctx, ActionParam.WithActor<P> actionParam);

    enum ActionType {
        init, join, isReady, cheatDeal,
        newTrick, // not sure we need this as an action to begin with
        play, pass
    }

}
