package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.websocket.Session;

@Value.Immutable
@JsonSerialize(as = ImmutableIncomingChatMessage.class)
@JsonDeserialize(as = ImmutableIncomingChatMessage.class)
public abstract class IncomingChatMessage implements IncomingMessage {
    public abstract String content();

    @Override
    public void accept(Session session, String roomId, MessageHandler visitor) {
        visitor.handle(session, roomId, this);
    }

}
