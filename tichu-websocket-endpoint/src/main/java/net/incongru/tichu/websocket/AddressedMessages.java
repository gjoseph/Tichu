package net.incongru.tichu.websocket;

import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of messages for each user in the game/room.
 */
class AddressedMessages {
    private final List<AddressedMessage> messages;

    public AddressedMessages() {
        this.messages = new LinkedList<>();
    }

    public void userMessage(UserId userId, OutgoingMessage message) {
        messages.add(new ImmutableAddressedMessage(userId, message));
    }

    public void roomMessage(OutgoingMessage message) {
        messages.add(new ImmutableAddressedMessage(null, message));
    }

    public List<AddressedMessage> getMessages() {
        return messages;
    }

    @Value.Immutable
    @Value.Style(of = "new")
    interface AddressedMessage {
        /**
         * The recipient for a particular message, null if the message should be sent to all users in the room.
         * TODO: maybe we want stronger typing for "room message", and maybe a single-user message still needs to be scoped to a room (if they play in multiple rooms)
         */
        @Value.Parameter
        @Nullable
        UserId recipient();

        @Value.Parameter
        @Nonnull
        OutgoingMessage message();
    }
}
