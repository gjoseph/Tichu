package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

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

    @Getter
    @ToString
    static public class Team {
        private final String name;
        private final Player player1;
        private final Player player2;

        public Team(String name, Player player1, Player player2) {
            Preconditions.checkNotNull(name, "Team name can't be null");
            Preconditions.checkNotNull(player1, "Player 1 for team %s can't be null", name);
            Preconditions.checkNotNull(player2, "Player 2 for team %s can't be null", name);
            this.name = name;
            this.player1 = player1;
            this.player2 = player2;
        }
    }

    @ToString
    static public class Player {
        private final String name;
        private final Set<Card> hand = new LinkedHashSet<>();
        private final Set<Card> wonCards = new LinkedHashSet<>();

        public Player(String name) {
            Preconditions.checkNotNull(name, "Player name can't be null");
            this.name = name;
        }

        public String name() {
            return name;
        }

        public void reclaimCards() {
            hand.clear();
            wonCards.clear();
        }

        public Set<Card> hand() {
            return Collections.unmodifiableSet(hand);
        }

        /**
         * The given card is dealt to this player.
         * OhMyTenses!
         */
        public void deal(Card card) {
            hand.add(card);
        }

        /**
         * This player picks up the given cards from the trick.
         * // TODO we may want to keep track of the various tricks instead of flattening ?
         */
        public void pickup(Set<Card> cards) {
            wonCards.addAll(cards);
        }

        public Set<Card> wonCards() {
            return Collections.unmodifiableSet(wonCards);
        }

        /**
         * The player has succesfully played these, we remove them from her hand.
         */
        void discard(Set<Card> cards) {
            if (!hand.containsAll(cards)) {
                throw new IllegalStateException("Could not remove cards " + cards + " from player hand " + hand);
            }
            // The boolean returned by removeAll does not indicate success, but rather that the collection was mutated
            // So it's false if cards is empty, OR if cards contains at least one card in hand, but doesn't validate all were in hand
            hand.removeAll(cards);
        }
    }
}
