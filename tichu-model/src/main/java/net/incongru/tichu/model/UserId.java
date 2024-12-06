package net.incongru.tichu.model;

/**
 * A UserId represents a user's... id. It might well be an external system's ID provided by e.g. Auth0 or Okta,
 * or in the case of a simulation a readable fake-human name.
 */
public record UserId(String id) {
    public static UserId of(String id) {
        return new UserId(id);
    }
}
