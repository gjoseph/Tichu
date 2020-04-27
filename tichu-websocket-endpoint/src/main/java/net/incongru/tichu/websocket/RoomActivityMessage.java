package net.incongru.tichu.websocket;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@Value.Style(stagedBuilder = true)
@JsonSerialize(as = ImmutableRoomActivityMessage.class)
@JsonDeserialize(as = ImmutableRoomActivityMessage.class)
public interface RoomActivityMessage extends OutgoingMessage {
    enum RoomActivity {
        CONNECTED, DISCONNECTED
    }

    @Nonnull
    UserId actor();

    @Nonnull
    RoomActivity activity();

}
