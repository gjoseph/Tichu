package net.incongru.tichu.websocket;

import javax.websocket.Session;

public class MessageHandlerImpl implements MessageHandler {
    private final SessionProvider sessions;

    public MessageHandlerImpl(SessionProvider sessions) {
        this.sessions = sessions;
    }

    @Override
    public void newSession(Session session, String roomId) {
        sessions.register(session);

        final String user = getUser(session);
        final ImmutableOutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(user)
                .content("Connected!")
                .build();

        sessions.broadcast(message);
    }

    @Override
    public void closeSession(Session session) {
        final OutgoingChatMessage outgoingChatMessage = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content("Disconnected!")
                .build();
        sessions.remove(session);
        sessions.broadcast(outgoingChatMessage);
    }

    @Override
    public void handle(Session session, IncomingChatMessage incomingMessage) {
        final OutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content(incomingMessage.content())
                .build();
        sessions.broadcast(message);
    }

    @Override
    public void handle(Session session, GameActionMessage gameActionMessage) {
        System.out.println("gameActionMessage = " + gameActionMessage);
        final OutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content("This is another message" + gameActionMessage.action())
                .build();
        sessions.broadcast(message);
    }

    private String getUser(Session session) {
        return session.getUserPrincipal().getName();
    }
}
