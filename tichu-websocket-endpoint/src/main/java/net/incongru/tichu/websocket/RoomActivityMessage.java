package net.incongru.tichu.websocket;

import javax.annotation.Nonnull;
import net.incongru.tichu.model.UserId;

public record RoomActivityMessage(
    @Nonnull UserId actor,
    @Nonnull RoomActivity activity
)
    implements OutgoingMessage {
    enum RoomActivity {
        CONNECTED,
        DISCONNECTED,
    }
}
