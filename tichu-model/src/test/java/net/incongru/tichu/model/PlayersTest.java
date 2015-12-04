package net.incongru.tichu.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlayersTest {
    @Test
    public void playerNamesMustBeUnique() {
        // TODO for now this will do be eventually we'll need a Player/Team-Builder of sorts
        assertThatThrownBy(() -> new Players("Greg", "Rufus", "G-R", "Greg", "Rufus", "I-C")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Players("Greg", "Rufus", "G-R", "greG", "Isa", "I-C")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void playerByNameIsCaseInsensitive() {
        final Players players = new Players("Greg", "Rufus", "G-R", "Isa", "Catherine", "I-C");
        assertThat(players.getPlayerByName("Salami")).isEmpty();
        assertThat(players.getPlayerByName("Isa")).hasValue(players.getPlayer(2));
        assertThat(players.getPlayerByName("isa")).hasValue(players.getPlayerByName("Isa").get());
    }
}