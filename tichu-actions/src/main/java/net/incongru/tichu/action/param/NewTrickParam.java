package net.incongru.tichu.action.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableNewTrickParam.class)
@JsonDeserialize(as = ImmutableNewTrickParam.class)
public interface NewTrickParam extends ActionParam {
    // TODO jackson mapping?

    // Convenience ...
    static NewTrickParam with() {
        return ImmutableNewTrickParam.builder().build();
    }
}
