package net.incongru.tichu.websocket;

import javax.websocket.server.ServerEndpointConfig;

public class EndpointConfigurator extends ServerEndpointConfig.Configurator {
    // TODO is there a guarantee the configurator is a singleton!? doesn't sound right.
    private final ChatStateProvider stateProvider = new ChatStateProvider();

    public EndpointConfigurator() {
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass != ChatEndpoint.class) {
            throw new IllegalStateException("This can only instantiate ChatEndpoint");
        }
        return (T) new ChatEndpoint(stateProvider);
    }
}
