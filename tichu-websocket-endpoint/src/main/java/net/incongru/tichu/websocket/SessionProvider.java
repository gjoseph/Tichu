package net.incongru.tichu.websocket;

import net.incongru.tichu.model.UserId;

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


    // TODO send to particular player _in a room_ (so a player could be in multiple rooms)
    void send(UserId userId, OutgoingMessage message) {
        // TODO not efficient at all obvs
        final Session session = sessions.stream().filter(s -> getUser(s).equals(userId)).findFirst().orElseThrow();
        sendMessage(session, message);
    }

    // TODO broadcast per-room (we may need both methods for system-wide broadcasts)
    void broadcast(OutgoingMessage message) {
        synchronized (sessions) {
            sessions.forEach(sesh -> sendMessage(sesh, message));
        }
    }

    private void sendMessage(Session session, OutgoingMessage message) {
        // Synchronising here for now -- see https://bz.apache.org/bugzilla/show_bug.cgi?id=56026
        synchronized (session) {
            session.getAsyncRemote().sendObject(message, result -> {
                if (!result.isOK()) {
                    // TODO metrics and logs
                    result.getException().printStackTrace();
                }
            });
        }
    }

    static UserId getUser(Session session) {
        return UserId.of(session.getUserPrincipal().getName());
    }
}
