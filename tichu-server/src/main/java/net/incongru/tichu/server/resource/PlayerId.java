package net.incongru.tichu.server.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Immutable;

/**
 *
 */
@Immutable
@JsonSerialize(as = ImmutablePlayerId.class)
@JsonDeserialize(as = ImmutablePlayerId.class)
interface PlayerId {
    @JsonProperty
    String uuid();

    @JsonProperty
    String displayName();
}
