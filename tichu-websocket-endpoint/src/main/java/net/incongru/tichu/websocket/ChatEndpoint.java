package net.incongru.tichu.websocket;

import net.incongru.tichu.websocket.codec.JacksonCodec;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

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

    private final ChatStateProvider store;

    public ChatEndpoint(ChatStateProvider store) {
        this.store = store;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        store.registerSession(session);
        // get username from auth
        System.out.println("session.getUserPrincipal() = " + session.getUserPrincipal());
        System.out.println("session.getUserProperties() = " + session.getUserProperties());
        store.addUser(session.getId(), username);

        final ImmutableOutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(username)
                .content("Connected!")
                .build();
        store.broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, IncomingMessage incomingMessage) throws IOException, EncodeException {
        System.out.println("message = " + incomingMessage);
        final MessageVisitor visitor = new MessageVisitorImpl(store);
        incomingMessage.accept(session, visitor);
    }

    static interface MessageVisitor {
        void handle(Session session, IncomingChatMessage incomingChatMessage);

        void handle(Session session, OtherThing otherThing);
    }

    static class MessageVisitorImpl implements MessageVisitor {
        private final ChatStateProvider store;

        public MessageVisitorImpl(ChatStateProvider store) {
            this.store = store;
        }

        @Override
        public void handle(Session session, IncomingChatMessage incomingMessage) {
            final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                    .from(store.getUser(session.getId()))
                    .content(incomingMessage.content())
                    .build();
            store.broadcast(message1);
        }

        public void handle(Session session, OtherThing otherThing) {
            System.out.println("otherThing = " + otherThing);
            final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                    .from(store.getUser(session.getId()))
                    .content("This is another message" + otherThing.thing())
                    .build();
            store.broadcast(message1);
            store.broadcast(ImmutableOtherThing.builder().thing("hello other thing").build());
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        store.removeSession(session);
        final OutgoingChatMessage outgoingChatMessage = ImmutableOutgoingChatMessage.builder()
                .from(store.getUser(session.getId()))
                .content("Disconnected!")
                .build();
        store.broadcast(outgoingChatMessage);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("throwable = " + throwable);
    }

}
