package net.incongru.tichu.websocket;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.action.impl.DefaultActionFactory;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.room.Room;
import net.incongru.tichu.room.RoomGameContext;
import net.incongru.tichu.room.RoomProvider;

import javax.websocket.Session;
import java.util.Optional;
import java.util.UUID;

import static net.incongru.tichu.action.impl.PlayerIsReadyResult.OK_STARTED;

public class MessageHandlerImpl implements MessageHandler {
    private final SessionProvider sessions;
    private final RoomProvider roomProvider;
    private final ActionFactory actionFactory;

    public MessageHandlerImpl(SessionProvider sessions, RoomProvider roomProvider) {
        this.sessions = sessions;
        this.roomProvider = roomProvider;
        this.actionFactory = new DefaultActionFactory();
    }

    @Override
    public void newSession(Session session, String roomId) {
        sessions.register(session);

        final UserId user = getUser(session);

        roomProvider.getRoom(roomId).enter(user);

        final OutgoingMessage msg = ImmutableRoomActivityMessage.builder()
                .actor(getUser(session))
                .activity(RoomActivityMessage.RoomActivity.CONNECTED)
                .build();

//        sessions.broadcast(roomId, message);
        sessions.broadcast(msg);
    }

    @Override
    public void closeSession(Session session, String roomId) {
        final OutgoingMessage msg = ImmutableRoomActivityMessage.builder()
                .actor(getUser(session))
                .activity(RoomActivityMessage.RoomActivity.DISCONNECTED)
                .build();
        // roomProvider.getRoom(roomId).leave(user);
        sessions.remove(session);
        sessions.broadcast(msg);
    }

    @Override
    public void handleError(Session session, Optional<String> clientTxId, Throwable throwable) {
        // TODO proper logs
        final String traceId = UUID.randomUUID().toString();
        System.out.println(String.format("Exception [%s]: %s ", traceId, throwable));
        throwable.printStackTrace();
        final ErrorMessage message = ImmutableErrorMessage.builder()
                .actor(getUser(session))
                .traceId(traceId)
                .clientTxId(clientTxId)
                .build();
        // TODO no need to broadcast to the world. Just to the user, or perhaps to the room.
        sessions.broadcast(message);
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

        // This will differ based on action/result
        // -- some need to be broadcast to all users
        // -- some to just the actor
        // -- some different message (values) to actor and other players
        // Actual result probably only sent to actor
        // Other table members just receive a log/view of it?

        final GameActionResultMessage msg = ImmutableGameActionResultMessage.builder()
                .clientTxId(gameActionMessage.clientTxId())
                .result(res)
                .build();
        sessions.broadcast(msg);

        // TODO not here
        if (res.result() == OK_STARTED) {
            final Game game = ctx.game();
            final Trick trick = game.currentRound().currentTrick();
            final Players players = game.players();
            players.stream().forEach(p -> {
                final Player.Hand hand = p.hand();
                System.out.println("player = " + p);
                System.out.println("hand = " + hand);
                final PlayerHandMessage playerHandMessage = ImmutablePlayerHandMessage.builder()
                        .clientTxId(gameActionMessage.clientTxId())
                        .hand(hand)
                        .build();
                System.out.println("playerHandMessage = " + playerHandMessage);

                // TODO abstract into SessionProvider
                // TODO Send to a particular player obviously
                session.getAsyncRemote().sendObject(playerHandMessage, result -> {
                    if (!result.isOK()) {
                        // TODO metrics and logs
                        result.getException().printStackTrace();
                    }
                });

            });
        }
    }

    private UserId getUser(Session session) {
        return UserId.of(session.getUserPrincipal().getName());
    }

}
