package net.incongru.tichu.websocket;

import javax.websocket.Session;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SessionProvider {

    // TODO probably not efficient at all, since Tomcat's Session doesn't implement hashcode..
    private Set<Session> sessions = new CopyOnWriteArraySet<>();

    void register(Session session) {
        sessions.add(session);
    }

    void remove(Session session) {
        sessions.remove(session);
    }

    void broadcast(Object message) { // TODO have _some_ base type here
        sessions.forEach(sesh -> {
            synchronized (sesh) {
                sesh.getAsyncRemote().sendObject(message);
            }
        });
    }
}
