package net.incongru.tichu.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Optional;
import net.incongru.tichu.websocket.codec.JacksonCodec;

@ServerEndpoint(
    value = RoomEndpoint.PATH + "/{roomId}",
    decoders = RoomEndpoint.IncomingMessageCodec.class,
    encoders = { RoomEndpoint.OutgoingMessageCodec.class },
    configurator = EndpointConfigurator.class
)
public class RoomEndpoint {

    public static final String PATH = "/room";

    public static class IncomingMessageCodec
        extends JacksonCodec<IncomingMessage> {}

    public static class OutgoingMessageCodec
        extends JacksonCodec<OutgoingMessage> {}

    private final MessageHandler messageHandler;

    public RoomEndpoint(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        messageHandler.newSession(session, roomId);
    }

    @OnMessage
    public void onMessage(
        Session session,
        @PathParam("roomId") String roomId,
        IncomingMessage incomingMessage
    ) {
        try {
            incomingMessage.accept(session, roomId, messageHandler);
        } catch (Exception e) {
            messageHandler.handleError(
                session,
                Optional.of(incomingMessage.clientTxId()),
                e
            );
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        messageHandler.closeSession(session, roomId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        messageHandler.handleError(session, Optional.empty(), throwable);
    }
}
