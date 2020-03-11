package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value.Immutable;

@Immutable
public interface InitialiseGameParam extends ActionParam {
    // TODO jackson mapping?

    // Convenience ...
    static InitialiseGameParam with() {
        return ImmutableInitialiseGameParam.builder().build();
    }
}
