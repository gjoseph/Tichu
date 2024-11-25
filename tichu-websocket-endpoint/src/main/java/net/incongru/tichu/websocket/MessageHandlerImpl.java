package net.incongru.tichu.websocket;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.ImmutableWithActor;
import net.incongru.tichu.action.impl.DefaultActionFactory;
import net.incongru.tichu.action.impl.NewTrickResult;
import net.incongru.tichu.action.impl.PlayerIsReadyResult;
import net.incongru.tichu.action.impl.PlayerPlaysResult;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Trick;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.room.Room;
import net.incongru.tichu.room.RoomGameContext;
import net.incongru.tichu.room.RoomProvider;

import jakarta.websocket.Session;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.incongru.tichu.websocket.SessionProvider.getUser;

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

        final OutgoingMessage msg = new RoomActivityMessage(
                getUser(session),
                RoomActivityMessage.RoomActivity.CONNECTED);

        // sessions.broadcast(roomId, message);
        sessions.broadcast(msg);
    }

    @Override
    public void closeSession(Session session, String roomId) {
        final OutgoingMessage msg = new RoomActivityMessage(
                getUser(session),
                RoomActivityMessage.RoomActivity.DISCONNECTED);
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
        final ErrorMessage message = new ErrorMessage(
                clientTxId,
                getUser(session),
                traceId
        );
        // TODO no need to broadcast to the world. Just to the user, or perhaps to the room.
        sessions.broadcast(message);
    }

    @Override
    public void handle(Session session, String roomId, IncomingChatMessage incomingMessage) {
        final OutgoingChatMessage message = ImmutableOutgoingChatMessage.builder()
                .from(getUser(session))
                .content(incomingMessage.content())
                .clientTxId(incomingMessage.clientTxId())
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

        System.out.println(String.format("%s => %s", withActor, res.result()));

        final AddressedMessages messageBundle = generateMessages(ctx, gameActionMessage, res);
        send(sessions, messageBundle);
    }

    /**
     * Generate different messages based on action and response.
     */
    protected AddressedMessages generateMessages(RoomGameContext ctx, GameActionMessage actionMessage, ActionResponse actionResponse) {
        final AddressedMessages messageBundle = new AddressedMessages();
        // Actual result should probably be only sent to actor
        // Other table members just receive a log/view of it?
        messageBundle.roomMessage(ImmutableGameActionResultMessage.builder()
                .clientTxId(actionMessage.clientTxId())
                .result(actionResponse)
                .build());

        if (actionResponse.result() == PlayerIsReadyResult.OK_STARTED
            || actionResponse.result() == PlayerPlaysResult.NEXT_PLAYER_GOES
            || actionResponse.result() == NewTrickResult.OK) {
            final Game game = ctx.game();
            final Trick trick = game.currentRound().currentTrick();
            final Players players = game.players();

            // every player's status, sent to everyone
            final Collection<GameStatusMessage.PlayerStatus> playerStatuses = players.stream()
                    .map(p -> {
                        final GameStatusMessage.PlayerState playerState = p.isReady() ? GameStatusMessage.PlayerState.READY : GameStatusMessage.PlayerState.NOT_READY;
                        return new GameStatusMessage.PlayerStatus(
                                p.id(),
                                playerState,
                                -1, // team TODO
                                p.hand().size(),
                                p.wonCards().size()
                        );
                    })
                    .collect(Collectors.toList());
            GameStatusMessage message = new GameStatusMessage(
                    playerStatuses,
                    trick.currentPlayer().id(),
                    // TODO enum?
                    trick.previousNonPass().play().name(),
                    // TODO
                    Collections.emptyList()
            );
            messageBundle.roomMessage(message);

            // TODO does this need to be a separate message, or merge it with game state?
            // own hand message for each player
            players.stream().forEach(p -> {
                final Player.Hand hand = p.hand();
                final PlayerHandMessage playerHandMessage = new PlayerHandMessage(
                        actionMessage.clientTxId(),
                        hand
                );
                messageBundle.userMessage(p.id(), playerHandMessage);
            });
        }

        return messageBundle;
    }

    private void send(SessionProvider sessions, AddressedMessages messageBundle) {
        messageBundle.getMessages().forEach(env -> {
            if (env.recipient() != null) {
                sessions.send(env.recipient(), env.message());
            } else {
                sessions.broadcast(env.message());
            }
        });
    }

}
