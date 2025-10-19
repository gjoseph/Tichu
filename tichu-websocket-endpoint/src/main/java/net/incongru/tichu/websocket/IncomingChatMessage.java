package net.incongru.tichu.websocket;

import jakarta.websocket.Session;

public record IncomingChatMessage(String clientTxId, String content) implements
    IncomingMessage {
    @Override
    public void accept(Session session, String roomId, MessageHandler visitor) {
        visitor.handle(session, roomId, this);
    }
}
