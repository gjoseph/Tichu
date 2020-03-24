package net.incongru.tichu.action.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableInitialiseGameParam.class)
@JsonDeserialize(as = ImmutableInitialiseGameParam.class)
public interface InitialiseGameParam extends ActionParam {
    // TODO jackson mapping?

    // Convenience ...
    static InitialiseGameParam with() {
        return ImmutableInitialiseGameParam.builder().build();
    }
}
