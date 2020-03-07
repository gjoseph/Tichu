package net.incongru.tichu.websocket;

import net.incongru.tichu.websocket.codec.MessageDecoder;
import net.incongru.tichu.websocket.codec.MessageEncoder;

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
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class,
        configurator = EndpointConfigurator.class
)
public class ChatEndpoint {
    public static final String PATH = "/chat";

    private final ChatStateProvider store;

    public ChatEndpoint(ChatStateProvider store) {
        this.store = store;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        store.registerSession(session);
        store.addUser(session.getId(), username);

        final ImmutableMessage message = ImmutableMessage.builder()
                .from(username)
                .content("Connected!")
                .build();
        store.broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        System.out.println("message = " + message);
        final ImmutableMessage message1 = ImmutableMessage.builder()
                .from(store.getUser(session.getId()))
                .content(message.content())
                .to(message.to())
                .build();
        store.broadcast(message1);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        store.removeSession(session);
        final Message message = ImmutableMessage.builder()
                .from(store.getUser(session.getId()))
                .content("Disconnected!")
                .build();
        store.broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("throwable = " + throwable);
    }

}
