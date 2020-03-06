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
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = ChatEndpoint.PATH + "/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class ChatEndpoint {
    public static final String PATH = "/chat";

    private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws EncodeException {
        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        final ImmutableMessage message = ImmutableMessage.builder()
                .from(username)
                .content("Connected!")
                .build();
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws EncodeException {
        System.out.println("message = " + message);
        final ImmutableMessage message1 = ImmutableMessage.builder()
                .from(users.get(session.getId()))
                .content(message.content())
                .to(message.to())
                .build();
        broadcast(message1);
    }

    @OnClose
    public void onClose(Session session) throws EncodeException {
        chatEndpoints.remove(this);
        final Message message = ImmutableMessage.builder()
                .from(users.get(session.getId()))
                .content("Disconnected!")
                .build();
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("throwable = " + throwable);
    }

    private static void broadcast(Message message) throws EncodeException {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
