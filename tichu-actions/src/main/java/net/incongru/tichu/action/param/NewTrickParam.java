package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value.Immutable;

@Immutable
public interface NewTrickParam extends ActionParam {
    // TODO jackson mapping?

    // Convenience ...
    static NewTrickParam with() {
        return ImmutableNewTrickParam.builder().build();
    }
}
