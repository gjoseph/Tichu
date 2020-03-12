package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.NewTrickParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;

import java.util.Set;
import java.util.function.Supplier;

// TODO this factory is for simulated/fake games. We probably need a different impl for real games.

public class ActionFactoryImpl implements ActionFactory {
    // GameContext should allow actions to e.g check for playerName validity
    // Maybe Actions will have a validate() method before they get executed.


    // Couldn't get a Map<Class<P>, Supplier<Action<P>>> to work (i.e without casting all over the place or impl my own map maybe)
    private static class KeyThing<P extends ActionParam> {
        final Class<P> paramClass;
        final Supplier<Action<P>> actionSupplier;

        private KeyThing(Class<P> paramClass, Supplier<Action<P>> actionSupplier) {
            this.paramClass = paramClass;
            this.actionSupplier = actionSupplier;
        }
    }

    private final Set<KeyThing> paramToActionSupplier;

    public ActionFactoryImpl() {
        this.paramToActionSupplier = Set.of(
                new KeyThing<>(JoinTableParam.class, JoinTable::new),
                new KeyThing<>(CheatDealParam.class, CheatDeal::new),
                new KeyThing<>(InitialiseGameParam.class, InitialiseGame::new),
                new KeyThing<>(JoinTableParam.class, JoinTable::new),
                new KeyThing<>(NewTrickParam.class, NewTrick::new),
                new KeyThing<>(PlayerIsReadyParam.class, PlayerIsReady::new),
                new KeyThing<>(PlayerPlaysParam.class, PlayerPlays::new)
        );
    }

    @Override
    public <P extends ActionParam> Action<P> actionFor(P param) {
        // there's gotta be a nicer way ...
        return ((KeyThing<P>) paramToActionSupplier
                .stream()
                .filter(k -> k.paramClass.isAssignableFrom(param.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No action for " + param.getClass().getSimpleName())))
                .actionSupplier
                .get();
    }

}
