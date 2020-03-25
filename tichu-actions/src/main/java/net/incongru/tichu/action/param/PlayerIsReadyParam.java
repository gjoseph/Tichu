package net.incongru.tichu.action.param;

import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

@Value.Immutable
public interface PlayerIsReadyParam extends ActionParam {
    String playerName();
    // TODO jackson mapping?

    // Convenience ...
    static PlayerIsReadyParam with(String playerName) {
        return ImmutablePlayerIsReadyParam.builder().playerName(playerName).build();
    }
}
