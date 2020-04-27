package net.incongru.tichu.websocket;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.incongru.tichu.model.UserId;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * {@link ErrorMessage}s have a trace ID which uniquely identifies the exception in logs.
 * They do not have a message, so as to not leak server-only information through poorly formed exception messages.
 * They can have a transaction ID, copied from the message they are a response to;
 * it must only be unique enough for the client to identify responses generated against this message.
 */
@Value.Immutable
@Value.Style(stagedBuilder = true)
@JsonSerialize(as = ImmutableErrorMessage.class)
@JsonDeserialize(as = ImmutableErrorMessage.class)
public interface ErrorMessage extends OutgoingMessage {

    @Nonnull
    Optional<String> txId();

    /**
     * The user whose message caused the exception
     */
    // TODO could exceptions be caused without an actor?
    @Nonnull
    UserId actor();

    @Nonnull
    String traceId();

}
