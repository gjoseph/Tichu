package net.incongru.tichu.model;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A UserId represents a user's... id. It might well be an external system's ID provided by e.g. Auth0 or Okta,
 * or in the case of a simulation a readable fake-human name.
 *
 * TODO @see UserDetailsProvider
 */
public class UserId {
    public static UserId of(String id) {
        return new UserId(id);
    }

    private final String id;

    private UserId(@Nonnull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return id.equals(userId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserId{" +
               "id='" + id + '\'' +
               '}';
    }
}
