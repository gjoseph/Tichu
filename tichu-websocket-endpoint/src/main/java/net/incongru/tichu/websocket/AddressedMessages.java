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
        @Value.Parameter
        @Nullable
        UserId recipient();

        @Value.Parameter
        @Nonnull
        OutgoingMessage message();
    }
}
