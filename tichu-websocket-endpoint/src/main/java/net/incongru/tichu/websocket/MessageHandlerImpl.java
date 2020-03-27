package net.incongru.tichu.websocket;

import javax.websocket.Session;

public class MessageHandlerImpl implements MessageHandler {
    private final ChatStateProvider store;

    public MessageHandlerImpl(ChatStateProvider store) {
        this.store = store;
    }

    @Override
    public void newSession(Session session, String username) {
        store.registerSession(session);
        // TODO get user from auth
        System.out.println("session.getUserPrincipal() = " + session.getUserPrincipal());
        System.out.println("session.getUserProperties() = " + session.getUserProperties());
        store.addUser(session.getId(), username);

        final ImmutableOutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(username)
                .content("Connected!")
                .build();

        // TODO This should probably not be in _store_
        store.broadcast(message);
    }

    @Override
    public void closeSession(Session session) {
        store.removeSession(session);
        final OutgoingChatMessage outgoingChatMessage = ImmutableOutgoingChatMessage.builder()
                .from(store.getUser(session.getId()))
                .content("Disconnected!")
                .build();
        store.broadcast(outgoingChatMessage);
    }

    @Override
    public void handle(Session session, IncomingChatMessage incomingMessage) {
        final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                .from(store.getUser(session.getId()))
                .content(incomingMessage.content())
                .build();
        store.broadcast(message1);
    }

    @Override
    public void handle(Session session, GameActionMessage gameActionMessage) {
        System.out.println("gameActionMessage = " + gameActionMessage);
        final ImmutableOutgoingChatMessage message1 = ImmutableOutgoingChatMessage.builder()
                .from(store.getUser(session.getId()))
                .content("This is another message" + gameActionMessage.action())
                .build();
        store.broadcast(message1);
    }
}
