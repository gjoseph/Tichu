package net.incongru.tichu.websocket;

import jakarta.websocket.Session;
import java.util.Optional;

// TODO this could become websocket-jsr356-agnostic if the 1st param was not Session but something agnostic
interface MessageHandler {
    void newSession(Session session, String roomId);

    void closeSession(Session session, String roomId);

    void handleError(Session session, Optional<String> clientTxId, Throwable e);

    // Visitor pattern below, see message implementations
    void handle(
        Session session,
        String roomId,
        IncomingChatMessage incomingChatMessage
    );

    void handle(
        Session session,
        String roomId,
        GameActionMessage gameActionMessage
    );
}
