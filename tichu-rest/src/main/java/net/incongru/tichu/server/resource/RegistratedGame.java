package net.incongru.tichu.server.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.incongru.tichu.model.Game;

/**
 * A game running in the server
 */
public class RegistratedGame {

    @JsonProperty
    private final String uuid;

    @JsonProperty
    private Table table;

    @JsonProperty
    private Game game; // TODO not sure this is what we'll expose!

    public RegistratedGame(String uuid) {
        this.uuid = uuid;
        this.table = new Table();
    }

    public String getUuid() {
        return uuid;
    }

    public Table getTable() {
        return table;
    }

    public Game getGame() {
        return game;
    }

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
}
