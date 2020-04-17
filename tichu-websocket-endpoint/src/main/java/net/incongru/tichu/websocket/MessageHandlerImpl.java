package net.incongru.tichu.websocket;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.action.impl.ActionFactoryImpl;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.room.Room;
import net.incongru.tichu.room.RoomGameContext;
import net.incongru.tichu.room.RoomProvider;

import javax.websocket.Session;

public class MessageHandlerImpl implements MessageHandler {
    private final SessionProvider sessions;
    private final RoomProvider roomProvider;
    private final ActionFactory actionFactory;

    public MessageHandlerImpl(SessionProvider sessions, RoomProvider roomProvider) {
        this.sessions = sessions;
        this.roomProvider = roomProvider;
        this.actionFactory = new ActionFactoryImpl();
    }

    @Override
    public void newSession(Session session, String roomId) {
        sessions.register(session);

        final UserId user = getUser(session);

        roomProvider.getRoom(roomId).enter(user);
        final ImmutableOutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(user)
                .content("Connected!")
                .build();

        sessions.broadcast(message);
    }

    @Override
    public void closeSession(Session session, String roomId) {
        final OutgoingChatMessage outgoingChatMessage = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content("Disconnected!")
                .build();
        sessions.remove(session);
        sessions.broadcast(outgoingChatMessage);
    }

    @Override
    public void handle(Session session, String roomId, IncomingChatMessage incomingMessage) {
        final OutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content(incomingMessage.content())
                .build();
        sessions.broadcast(message);
    }

    @Override
    public void handle(Session session, String roomId, GameActionMessage gameActionMessage) {
        final Room room = roomProvider.getRoom(roomId);
        final RoomGameContext ctx = room.gameContext();

        final UserId user = getUser(session);
        final ActionParam actionParam = gameActionMessage.action();
        final ActionParam.WithActor withActor = ImmutableWithActor.builder().actor(user).param(actionParam).build();
        final Action action = actionFactory.actionFor(actionParam);
        final ActionResponse res = action.exec(ctx, withActor);

        final GameActionResultMessage msg = ImmutableGameActionResultMessage.builder()
                .result(res)
                .build();
        sessions.broadcast(msg);
    }

    private UserId getUser(Session session) {
        return UserId.of(session.getUserPrincipal().getName());
    }

}
