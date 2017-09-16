package net.incongru.tichu.server.boot;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.incongru.tichu.server.health.ConfigHealthCheck;
import net.incongru.tichu.server.resource.TichuResource;

/**
 *
 */
public class TichuServerApp extends Application<TichuServerConfig> {
    public static void main(String[] args) throws Exception {
        new TichuServerApp().run(args);
    }

    @Override
    public String getName() {
        return "Tichu";
    }

    @Override
    public void initialize(Bootstrap<TichuServerConfig> bootstrap) {
//        environment.getApplicationContext().setContextPath("/api/"); // TODO would like to do this here instead of having a config file
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(TichuServerConfig configuration, Environment environment) {

        register(environment, new ConfigHealthCheck());

        final TichuResource tichuResource = new TichuResource();
        environment.jersey().register(tichuResource);
    }

    private void register(Environment environment, HealthCheck... checks) {
        for (HealthCheck check : checks) {
            environment.healthChecks().register(check.getClass().getSimpleName(), check);
        }
    }
}
