package net.incongru.tichu.action;

/**
 * @param <P> the type of {@link ActionParam} this action supports.
 * @see ActionFactory
 */
public interface Action<P extends ActionParam> {

    Class<P> paramType();

    // TODO add a description here or on param type

    ActionResult exec(GameContext ctx, P actionParam);

    enum ActionType {
        init, join, isReady, cheatDeal,
        play, pass
    }

}