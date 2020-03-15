package net.incongru.tichu.websocket;

import net.incongru.tichu.websocket.codec.JacksonCodec;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = ChatEndpoint.PATH + "/{username}",
        decoders = ChatEndpoint.IncomingMessageCodec.class,
        encoders = {ChatEndpoint.OutgoingChatMessageCodec.class, ChatEndpoint.OtherThingCodec.class},
        configurator = EndpointConfigurator.class
)
public class ChatEndpoint {
    public static final String PATH = "/chat";

    public static class IncomingMessageCodec extends JacksonCodec<IncomingMessage> {
    }

    public static class OtherThingCodec extends JacksonCodec<OtherThing> {
    }

    public static class OutgoingChatMessageCodec extends JacksonCodec<OutgoingChatMessage> {
    }

    private final MessageHandler messageHandler;

    public ChatEndpoint(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        messageHandler.newSession(session, username);
    }

    @OnMessage
    public void onMessage(Session session, IncomingMessage incomingMessage) {
        incomingMessage.accept(session, messageHandler);
    }

    @OnClose
    public void onClose(Session session) {
        messageHandler.closeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("throwable = " + throwable);
    }

}
