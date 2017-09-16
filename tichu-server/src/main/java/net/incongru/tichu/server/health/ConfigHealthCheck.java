package net.incongru.tichu.server.health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 */
public class ConfigHealthCheck extends HealthCheck {

    public ConfigHealthCheck() {
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
