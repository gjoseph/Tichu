package net.incongru.tichu.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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

    @Getter
    @ToString
    static public class Player {
        private final String name;
        private final Set<Card> hand = new LinkedHashSet<>();
        private final Set<Card> wonCards = new LinkedHashSet<>();
        private Announce announce;

        public Player(String name) {
            Preconditions.checkNotNull(name, "Player name can't be null");
            this.name = name;
        }

        public void reclaimCards() {
            hand.clear();
            wonCards.clear();
            announce = null;
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

        protected void announce(Announce announce) {
            this.announce = announce;
        }
    }
}
