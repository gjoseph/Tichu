package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.websocket.Session;

@Value.Immutable
@JsonSerialize(as = ImmutableIncomingChatMessage.class)
@JsonDeserialize(as = ImmutableIncomingChatMessage.class)
public abstract class IncomingChatMessage implements IncomingMessage {
    abstract String content();

    public void accept(Session session, MessageHandler visitor) {
        visitor.handle(session, this);
    }

}
