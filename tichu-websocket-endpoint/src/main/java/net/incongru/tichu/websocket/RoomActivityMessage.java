package net.incongru.tichu.websocket;

import net.incongru.tichu.model.UserId;

import javax.annotation.Nonnull;

public record RoomActivityMessage(
        @Nonnull UserId actor,
        @Nonnull RoomActivity activity
) implements OutgoingMessage {
    enum RoomActivity {
        CONNECTED, DISCONNECTED
    }

}
