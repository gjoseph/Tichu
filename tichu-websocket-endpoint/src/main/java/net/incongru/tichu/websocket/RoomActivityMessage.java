package net.incongru.tichu.websocket;

import net.incongru.tichu.model.UserId;
import org.jspecify.annotations.NonNull;

public record RoomActivityMessage(
    @NonNull UserId actor,
    @NonNull RoomActivity activity
) implements OutgoingMessage {
    enum RoomActivity {
        CONNECTED,
        DISCONNECTED,
    }
}
