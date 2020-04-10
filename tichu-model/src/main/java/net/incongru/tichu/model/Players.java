package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the players and teams
 */
public class Players {
    private final List<Player> players = new ArrayList<>(4);
    private final Team[] teams = new Team[2];

    public Players() {
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
        optGetPlayer(p.id()).ifPresentOrElse(
                ignore -> {
                    throw new IllegalArgumentException("Player " + p + " has already joined");
                },
                () -> {
                    t.join(p);
                    players.add(p);
                });
    }

    public boolean isComplete() {
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
        if (!players.contains(whoStarts)) {
            throw new IllegalStateException(whoStarts + " is not at this table");
        }
        final List<Player> playersOrder = new ArrayList<>();
        // ... well this works, sure, but player order will never change across the whole game so this feels silly. And won't work with alternative rules.
        playersOrder.add(teams[0].player(0));
        playersOrder.add(teams[1].player(0));
        playersOrder.add(teams[0].player(1));
        playersOrder.add(teams[1].player(1));

        final PeekingIterator<Player> cycle = Iterators.peekingIterator(Iterators.cycle(playersOrder));
        // Skip until we find the right player
        while (cycle.peek() != whoStarts) {
            cycle.next();
        }
        return cycle;
    }

    public Player getPlayerById(UserId id) {
        return optGetPlayer(id).orElseThrow(() -> new IllegalArgumentException("No player with ID " + id));
    }

    private Optional<Player> optGetPlayer(UserId id) {
        return stream()
                .filter(player -> player.id().equals(id))
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
