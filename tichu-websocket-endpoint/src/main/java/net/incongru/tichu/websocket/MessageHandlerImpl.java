package net.incongru.tichu.websocket;

import javax.websocket.Session;

public class MessageHandlerImpl implements MessageHandler {
    private final SessionProvider sessions;

    public MessageHandlerImpl(SessionProvider sessions) {
        this.sessions = sessions;
    }

    @Override
    public void newSession(Session session, String username) {
        sessions.register(session);
        // TODO get user from auth
        System.out.println("session.getUserPrincipal() = " + session.getUserPrincipal());
        System.out.println("session.getUserProperties() = " + session.getUserProperties());
        sessions.addUser(session.getId(), username);

        final ImmutableOutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(username)
                .content("Connected!")
                .build();

        // TODO This should probably not be in _store_
        sessions.broadcast(message);
    }

    @Override
    public void closeSession(Session session) {
        sessions.remove(session);
        final OutgoingChatMessage outgoingChatMessage = ImmutableOutgoingChatMessage.builder()
                .from(sessions.getUser(session.getId()))
                .content("Disconnected!")
                .build();
        sessions.broadcast(outgoingChatMessage);
    }

    @Override
    public void handle(Session session, IncomingChatMessage incomingMessage) {
        final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                .from(sessions.getUser(session.getId()))
                .content(incomingMessage.content())
                .build();
        sessions.broadcast(message1);
    }

    @Override
    public void handle(Session session, GameActionMessage gameActionMessage) {
        System.out.println("gameActionMessage = " + gameActionMessage);
        final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                .from(sessions.getUser(session.getId()))
                .content("This is another message" + gameActionMessage.action())
                .build();
        sessions.broadcast(message1);
    }
}
