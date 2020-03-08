package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.websocket.Session;

@Value.Immutable
@JsonSerialize(as = ImmutableOtherThing.class)
@JsonDeserialize(as = ImmutableOtherThing.class)
public abstract class OtherThing implements IncomingMessage {
    abstract String thing();

    public void accept(Session session, MessageHandler visitor) {
        visitor.handle(session, this);
    }

}
