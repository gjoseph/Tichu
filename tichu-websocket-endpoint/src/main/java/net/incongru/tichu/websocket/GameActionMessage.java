package net.incongru.tichu.websocket;

import jakarta.websocket.Session;
import net.incongru.tichu.action.ActionParam;

public record GameActionMessage(
    String clientTxId,
    //    @JsonUnwrapped -- TODO would be nice but fucks with typing
    ActionParam action
)
    implements IncomingMessage {
    @Override
    public void accept(Session session, String roomId, MessageHandler visitor) {
        visitor.handle(session, roomId, this);
    }
}
