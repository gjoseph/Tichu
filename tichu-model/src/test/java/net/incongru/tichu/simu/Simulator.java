package net.incongru.tichu.simu;

import com.google.common.collect.Sets;
import lombok.Value;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.DeckConstants;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Round;
import net.incongru.tichu.model.TichuRules;
import net.incongru.tichu.model.Trick;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Loads up a game round described in a yaml file (to give it _some_ structure and parse easily cause i'm lazy)
 * and simulates it. See {@link SimulatorTest} which verifies simulation results.
 *
 */
public class Simulator {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Simulator.class);

    private static final HashSet<String> playerKeys = Sets.newHashSet("p1", "p2", "p3", "p4");

    public static void simulateFrom(String resourcePath) {
        simulateFrom(Simulator.class.getResourceAsStream(resourcePath));
    }

    public static void simulateFrom(InputStream in) {

        final Players players = new Players("p1", "p3", "t13", "p2", "p4", "t24");
        final Simulation simu = loadAndParse(players, in);

        final TichuRules rules = new TichuRules() {
            @Override
            public CardDeck newShuffledDeck() {
                return new CardDeck() {
                    @Override
                    protected List<Card> shuffle(Set<Card> cards) {
                        // We don't shuffle, since we want to control the draft
                        return simu.getFakeDraft();
                    }
                };
            }
        };

        final Game game = new Game(players, rules);
        final Round round = game.start();
        final Trick trick = round.start();

        for (SimulatedPlay play : simu.getPlays()) {
            final Players.Player p = play.getPlayer();
            final Set<Card> cardsToPlay = play.getCardsPlayed();
            log.info("{} plays {}", p.name(), cardsToPlay);
            final Play validatedPlay = rules.validate(cardsToPlay);
            // TODO validatedPlay is maybe null if the set of played cards was invalid
            final Play.PlayResult playResult = trick.play(p, cardsToPlay);
            log.info("> {} -> {}", play, playResult);

            //TODO if (playResult.getResult() != Trick.PlayResult.Result.NEXTGOES) {
            //TODO simu should somehow mark when we expect a trick to be finished
            assertThat(playResult.getResult()).isEqualTo(play.expectedResult);
        }

        assertThat(trick).matches(Trick::isDone, "Trick should be done");

        // Do we need to simulate more than 1 round ?
        if (simu.getExpectedScore() != null) {
            assertThat(round.score()).isEqualTo(simu.expectedScore);
        }

        // TODO: assert error
    }

    private static Simulation loadAndParse(Players players, InputStream in) {
        final Map<String, Players.Player> playerByName = players.stream().collect(Collectors.toMap(Players.Player::name, Function.identity()));

        final Map map = new Yaml().loadAs(in, Map.class);
        final Map<String, String> draftStr = (Map<String, String>) map.get("draft");
        check(draftStr.keySet().equals(playerKeys), "draft keys should be p1, p2, p3 and p4");

        // We flatten the list of cards-per-player so that we can simply let Round.shuffleAndDeal() do its thing (but we make sure we control the order of the cards)
        final List<Card> sortedDraft = draftStr.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .map(Simulator::parseCardSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        final ArrayList<Card> deckCheck = new ArrayList<>(new CardDeck().allRemaining());
        deckCheck.removeAll(sortedDraft);
        check(deckCheck.isEmpty(), "Not all cards have been drafted in this simulation. Remaining: " + deckCheck);
        check(sortedDraft.size() == 56, "Not all cards have been drafted in this simulation - not 56 cards but not sure which one's missing");

        final String allPlaysStr = (String) map.get("plays");

        final List<SimulatedPlay> simulatedPlays = split(allPlaysStr, "\n").map(playStr -> {
            final String[] objects = split(playStr, "(- |:|->)").toArray(String[]::new);
            return new SimulatedPlay(
                    playerByName.get(objects[0]),
                    parseCardSet(objects[1]),
                    Play.PlayResult.Result.valueOf(objects[2])
            );
        }).collect(toList());
        final String expectedError = (String) map.get("expectedError");
        final String expectedScoreStr = (String) map.get("expectedScore");

        check(expectedError != null ^ expectedScoreStr != null, "Only one of expectedScore or expectedError should be defined");

        final Integer[] scores = split(expectedScoreStr, "[/,-]").map(Integer::parseInt).limit(2).toArray(Integer[]::new);

        return new Simulation(sortedDraft, simulatedPlays, new Round.Score(scores[0], scores[1]), expectedError);
    }

    private static Stream<String> split(String src, String regex) {
        return Arrays.stream(src.split(regex)).map(String::trim).filter(s -> !s.isEmpty());
    }

    private static Set<Card> parseCardSet(String cardSetStr) {
        // Rely on DeckConstants constant names
        return split(cardSetStr, ",").map(DeckConstants::byName).collect(Collectors.toSet());
    }

    private static void check(boolean b, String msg) {
        if (!b) {
            throw new IllegalStateException(msg);
        }
    }

    @Value
    public static class Simulation {
        private final List<Card> fakeDraft; // all cards of player1, followed by all cards of player2, etc.
        private final List<SimulatedPlay> plays;
        private final Round.Score expectedScore;
        private final String expectedError;
    }

    @Value
    public static class Draft {
        private final Players.Player player;
        private final Set<Card> initialHand;
    }

    @Value
    public static class SimulatedPlay {
        private final Players.Player player;
        private final Set<Card> cardsPlayed;
        private final Play.PlayResult.Result expectedResult;
    }

}
