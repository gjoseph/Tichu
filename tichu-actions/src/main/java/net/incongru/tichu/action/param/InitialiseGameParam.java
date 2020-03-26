package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
public interface InitialiseGameParam extends ActionParam {

    // Convenience ...
    static InitialiseGameParam with() {
        return ImmutableInitialiseGameParam.builder().build();
    }
}
