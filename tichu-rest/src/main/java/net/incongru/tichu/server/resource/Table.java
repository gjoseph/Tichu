package net.incongru.tichu.server.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

class Table {

    @JsonProperty
    private final PlayerId[] players = new PlayerId[4];

    /**
     * @param pos 0-indexed
     */
    public void sit(int pos, PlayerId p) {
        if (players[pos] != null) {
            throw new IllegalStateException(
                "Chair #" + pos + " is already taken"
            );
        }
        players[pos] = p;
    }
}
