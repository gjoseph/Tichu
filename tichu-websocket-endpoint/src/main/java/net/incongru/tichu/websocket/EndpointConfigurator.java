package net.incongru.tichu.websocket;

import jakarta.websocket.server.ServerEndpointConfig;
import net.incongru.tichu.room.DummyRoomProvider;
import net.incongru.tichu.room.RoomProvider;

public class EndpointConfigurator extends ServerEndpointConfig.Configurator {

    // TODO is there a guarantee the configurator is a singleton!? doesn't sound right.
    private final SessionProvider sessionProvider = new SessionProvider();
    private final RoomProvider roomProvider = new DummyRoomProvider();

    public EndpointConfigurator() {}

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass)
        throws InstantiationException {
        if (endpointClass != RoomEndpoint.class) {
            throw new IllegalStateException(
                "This can only instantiate RoomEndpoint"
            );
        }
        return (T) new RoomEndpoint(
            new MessageHandlerImpl(sessionProvider, roomProvider)
        );
    }
}
