package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public Players() {
    }

    private void ensureNoDuplicateNames() {
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

    public void add(Team t) {
        Objects.requireNonNull(t, "Team can't be null");
        if (teams[0] == null) {
            teams[0] = t;
        } else if (teams[1] == null) {
            teams[1] = t;
        } else {
            throw new IllegalStateException("Enough teams have joined");
        }
    }

    public void join(Player p, Team t) {
        if (isComplete()) {
            throw new IllegalStateException("All players have joined");
        }
//        if (Arrays.binarySearch(teams, t) < 0) { TODO
//            throw new IllegalArgumentException("Team " + t + " is not at this table");
//        }
        t.join(p);
        players.add(p);
    }

    public boolean isComplete() {
        // Ensure names are unique
        ensureNoDuplicateNames();
        if (teams.length != 2) {
            return false;
        }
        for (Team team : teams) {
            if (team == null || !team.isComplete()) {
                return false;
            }
        }
        return players.size() == 4;
    }

    public boolean areAllReady() {
        return isComplete() && stream().allMatch(Player::isReady);
    }

    // TODO i don't like this method
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
     * 0-indexed.
     */
    public Player getPlayer(int i) {
        Preconditions.checkArgument(0 <= i && i < 4);
        return players.get(i);
    }

    public Optional<Player> getPlayerByName(String name) {
        return stream()
                .filter(player -> player.name().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * 0-indexed.
     */
    public Team getTeam(int i) {
        Preconditions.checkArgument(0 <= i && i < 2);
        return teams[i];
    }

}
