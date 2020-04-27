package net.incongru.tichu.websocket;

import javax.websocket.Session;

// TODO this could become websocket-jsr356-agnostic if the 1st param was not Session but something agnostic
interface MessageHandler {
    void newSession(Session session, String roomId);

    void closeSession(Session session, String roomId);

    void handleError(Session session, Throwable throwable);

    // Visitor pattern below, see message implementations
    void handle(Session session, String roomId, IncomingChatMessage incomingChatMessage);

    void handle(Session session, String roomId, GameActionMessage gameActionMessage);
}
