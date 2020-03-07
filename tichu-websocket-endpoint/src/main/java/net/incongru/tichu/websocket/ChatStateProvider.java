package net.incongru.tichu.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class ChatStateProvider {

    private Set<Session> sessions = new CopyOnWriteArraySet<>();
    private HashMap<String, String> users = new HashMap<>();

    void registerSession(Session session) {
        sessions.add(session);
    }

    void removeSession(Session session) {
        sessions.remove(session);
    }

    String getUser(String sessionId) {
        return users.get(sessionId);
    }

    void addUser(String sessionId, String username) {
        users.put(sessionId, username);
    }

    void broadcast(Message message) throws IOException, EncodeException {
        sessions.forEach(unchecked(sesh -> {
            synchronized (sesh) {
                sesh.getBasicRemote().sendObject(message);
            }
        }));
    }

    @FunctionalInterface
    private static interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }

    private static <T> Consumer<T> unchecked(ThrowingConsumer<T> f) {
        return t -> {
            try {
                f.accept(t);
            } catch (Exception ex) {
                sneakyThrow(ex);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception, R> R sneakyThrow(Exception t) throws T {
        throw (T) t; // ( ͡° ͜ʖ ͡°)
    }
}
