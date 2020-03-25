package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.action.ActionParam;
import org.immutables.value.Value;

import javax.websocket.Session;

@Value.Immutable
@JsonSerialize(as = ImmutableGameActionMessage.class)
@JsonDeserialize(as = ImmutableGameActionMessage.class)
public abstract class GameActionMessage implements IncomingMessage {
    //    @JsonUnwrapped -- TODO would be nice but fucks with typing
    public abstract ActionParam action();

    public void accept(Session session, MessageHandler visitor) {
        visitor.handle(session, this);
    }

}
