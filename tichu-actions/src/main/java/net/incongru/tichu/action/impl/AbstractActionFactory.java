package net.incongru.tichu.action.impl;

import java.util.Set;
import java.util.function.Supplier;
import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;

// TODO GameContext should allow actions to e.g check for playerName validity
// Maybe Actions will have a validate() method before they get executed.

public abstract class AbstractActionFactory implements ActionFactory {

    // Since the param passed to actionFor() isn't a direct instance of the Param class we keep here,
    // we need to iterate anyway, so using a Set instead of map here, which is somewhat
    // simpler for generics reasons.
    private final Set<Key<?>> paramToActionSupplier;

    public AbstractActionFactory() {
        this.paramToActionSupplier = build();
    }

    protected abstract Set<Key<?>> build();

    protected <P extends ActionParam> Key<P> key(
        Class<P> paramClass,
        Supplier<Action<P, ?>> actionSupplier
    ) {
        return new Key<>(paramClass, actionSupplier);
    }

    @Override
    public <P extends ActionParam> Action<
        P,
        ? extends ActionResponse.Result
    > actionFor(P param) {
        return (
            (Key<P>) paramToActionSupplier
                .stream()
                .filter(k -> k.paramClass.isAssignableFrom(param.getClass()))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "No action for " + param.getClass().getSimpleName()
                    )
                )
        ).actionSupplier.get();
    }

    protected static class Key<P extends ActionParam> {

        final Class<P> paramClass;
        final Supplier<Action<P, ?>> actionSupplier;

        private Key(
            Class<P> paramClass,
            Supplier<Action<P, ?>> actionSupplier
        ) {
            this.paramClass = paramClass;
            this.actionSupplier = actionSupplier;
        }
    }
}
