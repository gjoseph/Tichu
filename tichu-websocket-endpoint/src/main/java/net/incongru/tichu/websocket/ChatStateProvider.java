package net.incongru.tichu.websocket;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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

    void broadcast(Object message) { // TODO have _some_ base type here
        sessions.forEach(sesh -> {
            synchronized (sesh) {
                sesh.getAsyncRemote().sendObject(message);
            }
        });
    }
}
