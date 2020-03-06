package net.incongru.tichu.websocket;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@Value.Style(strictBuilder = true) // disables the "from" method in builder
@JsonSerialize(as = ImmutableMessage.class) // could get right of this if we have a custom meta annotation, apparently
@JsonDeserialize(as = ImmutableMessage.class)
public interface Message {
    @Nullable
    String from();

    @Nullable
    String to();

    String content();
}
