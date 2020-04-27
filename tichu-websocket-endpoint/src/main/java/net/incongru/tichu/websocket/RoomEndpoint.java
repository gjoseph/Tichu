package net.incongru.tichu.websocket;

import net.incongru.tichu.websocket.codec.JacksonCodec;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Optional;

@ServerEndpoint(value = RoomEndpoint.PATH + "/{roomId}",
        decoders = RoomEndpoint.IncomingMessageCodec.class,
        encoders = {RoomEndpoint.OutgoingMessageCodec.class},
        configurator = EndpointConfigurator.class
)
public class RoomEndpoint {
    public static final String PATH = "/room";

    public static class IncomingMessageCodec extends JacksonCodec<IncomingMessage> {
    }

    public static class OutgoingMessageCodec extends JacksonCodec<OutgoingMessage> {
    }

    private final MessageHandler messageHandler;

    public RoomEndpoint(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        messageHandler.newSession(session, roomId);
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("roomId") String roomId, IncomingMessage incomingMessage) {
        try {
            incomingMessage.accept(session, roomId, messageHandler);
        } catch (Exception e) {
            messageHandler.handleError(session, Optional.of(incomingMessage.clientTxId()), e);
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
