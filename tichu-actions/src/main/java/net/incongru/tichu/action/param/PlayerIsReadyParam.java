package net.incongru.tichu.action.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePlayerIsReadyParam.class)
@JsonDeserialize(as = ImmutablePlayerIsReadyParam.class)
public interface PlayerIsReadyParam extends ActionParam {
    String playerName();
    // TODO jackson mapping?

    // Convenience ...
    static PlayerIsReadyParam with(String playerName) {
        return ImmutablePlayerIsReadyParam.builder().playerName(playerName).build();
    }
}
