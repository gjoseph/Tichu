package net.incongru.tichu.websocket;

import java.util.Optional;
import javax.annotation.Nonnull;
import net.incongru.tichu.model.UserId;

/**
 * {@link ErrorMessage}s have a trace ID which uniquely identifies the exception in logs.
 * They do not have a message, so as to not leak server-only information through poorly formed exception messages.
 * They can have a transaction ID, copied from the message they are a response to;
 * it must only be unique enough for the client to identify responses generated against this message.
 *
 * @param actor The user whose message caused the exception.
 */
public record ErrorMessage(
    @Nonnull Optional<String> clientTxId,
    // TODO could exceptions be caused without an actor?
    @Nonnull UserId actor,
    @Nonnull String traceId
)
    implements OutgoingMessage {}
