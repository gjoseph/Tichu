package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Represents the players and teams
 */
public class Players {
    private final List<Player> players = new ArrayList<>(4);
    private final Team[] teams = new Team[2];

    // TODO this API is shit
    public Players(String player1Name, String player3Name, String team13Name, String player2Name, String player4Name, String team24Name) {
        players.add(0, new Player(player1Name));
        players.add(1, new Player(player2Name));
        players.add(2, new Player(player3Name));
        players.add(3, new Player(player4Name));
        teams[0] = new Team(team13Name, players.get(0), players.get(2));
        teams[1] = new Team(team24Name, players.get(1), players.get(3));

        // Ensure names are unique
        final List<String> dupes = players.stream() // Meh....
                .map(Player::name)
                .map(String::toLowerCase)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(toList());
        if (!dupes.isEmpty()) {
            throw new IllegalArgumentException("There can only be one " + dupes);
        }

    }

    public Stream<Player> stream() {
        return players.stream();
    }

    public Iterator<Player> cycleFrom(Player whoStarts) {
        final PeekingIterator<Player> cycle = Iterators.peekingIterator(Iterators.cycle(players));
        while (cycle.next() != whoStarts) {
            //
        }
        return cycle;
    }

    /**
     * Index starts at 1 ?
     */
    public Player getPlayer(int i) {
        Preconditions.checkArgument(1 <= i && i <= 4);
        return players.get(i - 1);
    }

    public Optional<Player> getPlayerByName(String name) {
        return stream()
                .filter(player -> player.name().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Index starts at 1 ?
     */
    public Team getTeam(int i) {
        Preconditions.checkArgument(1 <= i && i >= 2);
        return teams[i - 1];
    }

}
