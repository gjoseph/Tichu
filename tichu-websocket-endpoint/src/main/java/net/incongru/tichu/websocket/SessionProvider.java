package net.incongru.tichu.websocket;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;
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

    // TODO broadcast per-room (we may need both method for system-wide broadcasts)
    void broadcast(OutgoingMessage message) {
        sessions.forEach(sesh -> {
            synchronized (sesh) {
                sesh.getAsyncRemote().sendObject(message, new SendHandler() {
                    @Override
                    public void onResult(SendResult result) {
                        System.out.println("result = " + result);
                        if (!result.isOK()) {
                            // TODO metrics and logs
                            result.getException().printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
