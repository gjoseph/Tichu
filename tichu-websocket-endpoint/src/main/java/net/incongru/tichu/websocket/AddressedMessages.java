package net.incongru.tichu.websocket;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.incongru.tichu.model.UserId;

/**
 * A collection of messages for each user in the game/room.
 */
class AddressedMessages {

    private final List<AddressedMessage> messages;

    public AddressedMessages() {
        this.messages = new LinkedList<>();
    }

    public void userMessage(UserId userId, OutgoingMessage message) {
        messages.add(new AddressedMessage(userId, message));
    }

    public void roomMessage(OutgoingMessage message) {
        messages.add(new AddressedMessage(null, message));
    }

    public List<AddressedMessage> getMessages() {
        return messages;
    }

    /**
     * @param recipient The recipient for a particular message, null if the message should be sent to all users in the room.
     *                                   TODO: maybe we want stronger typing for "room message", and maybe a single-user message still needs to be scoped to a room (if they play in multiple rooms)
     */
    record AddressedMessage(
        @Nullable UserId recipient,
        @Nonnull OutgoingMessage message
    ) {}
}
