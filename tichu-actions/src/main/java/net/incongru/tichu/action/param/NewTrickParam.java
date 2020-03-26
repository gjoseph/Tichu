package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
public interface NewTrickParam extends ActionParam {

    // Convenience ...
    static NewTrickParam with() {
        return ImmutableNewTrickParam.builder().build();
    }
}
