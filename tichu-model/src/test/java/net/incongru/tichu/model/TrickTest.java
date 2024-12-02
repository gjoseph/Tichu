package net.incongru.tichu.model;

import static java.util.Collections.emptySet;
import static net.incongru.tichu.model.TestUtil.samplePlayers;
import static net.incongru.tichu.model.UserId.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Sets;
import java.util.Collections;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TrickTest {

    private final TichuRules tichuRules = new TichuRules();
    private Players players;
    private Player alex, charlie, jules, quinn;

    @BeforeEach
    void setUp() {
        players = samplePlayers();
        alex = players.getPlayerById(of("Alex"));
        charlie = players.getPlayerById(of("Charlie"));
        jules = players.getPlayerById(of("Jules"));
        quinn = players.getPlayerById(of("Quinn"));
    }

    @Test
    void isDoneOnceEveryBodyPassed() {
        final Trick trick = newTrickFromPlayer1(players);
        alex.deal(DeckConstants.B2);

        // 1st player plays
        assertThat(
            trick.play(alex, Sets.newHashSet(DeckConstants.B2)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);

        // players 2 and 3 pass, we're not done yet
        assertThat(trick.play(charlie, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertThat(trick.play(jules, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        // 4th player passes, we're done
        assertThat(trick.play(quinn, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.TRICK_END
        );
        assertTrue(trick.isDone());
    }

    @Test
    void allPassIsNotDone() {
        final Trick trick = newTrickFromPlayer1(players);
        assertThat(trick.play(alex, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        assertThat(trick.play(charlie, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        assertThat(trick.play(jules, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        assertThat(trick.play(quinn, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        // and still not done when coming back to 1st player
        assertThat(trick.play(alex, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());
    }

    @Test
    void prevPlayReturnsPreviousNonPassPlay() {
        final Trick trick = newTrickFromPlayer1(players);
        alex.deal(DeckConstants.B2);
        quinn.deal(DeckConstants.G5);

        // 1st player plays
        assertThat(
            trick.play(alex, Sets.newHashSet(DeckConstants.B2)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);

        // players 2 and 3 pass, we're not done yet
        assertThat(trick.play(charlie, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertThat(trick.play(jules, emptySet()).result()).isEqualTo(
            Play.PlayResult.Result.NEXTGOES
        );
        assertFalse(trick.isDone());

        // previous non-pass play is player 1's single
        assertThat(trick.previousNonPass().play().getCards()).isEqualTo(
            Collections.singleton(DeckConstants.B2)
        );
        // player 4 plays another single, that's valid
        assertThat(
            trick.play(quinn, Sets.newHashSet(DeckConstants.G5)).result()
        ).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertFalse(trick.isDone());
    }

    @Test
    void cantPlayDogAfterOtherCard() {
        alex.deal(DeckConstants.Star_2);
        charlie.deal(DeckConstants.Dog);
        final Trick trick = newTrickFromPlayer1(players);

        final Play.PlayResult play1 = trick.play(
            alex,
            Collections.singleton(DeckConstants.Star_2)
        );
        assertThat(play1.result()).isEqualTo(Play.PlayResult.Result.NEXTGOES);
        assertThat(trick.currentPlayer()).isEqualTo(charlie);

        final Play.PlayResult play2 = trick.play(
            charlie,
            Collections.singleton(DeckConstants.Dog)
        );
        assertThat(play2.result()).isEqualTo(Play.PlayResult.Result.TOOWEAK); // TODO should it be invalid?
        assertThat(trick.currentPlayer()).isEqualTo(charlie);
    }

    @Test
    void playingDogSkipsToTeamPartner() {
        final Trick trick = newTrickFromPlayer1(players);
        alex.deal(DeckConstants.Dog);

        final Play.PlayResult play = trick.play(
            alex,
            Collections.singleton(DeckConstants.Dog)
        );
        assertThat(play.result()).isEqualTo(Play.PlayResult.Result.TRICK_END);
        assertThat(trick.currentPlayer()).isEqualTo(jules);
    }

    @Test
    @Disabled(
        "Currently assuming empty-handed players will 'pass', which is probably not correct ..."
    )
    void playingDogSkipsToNextPlayerTeamPartnerIsDone() {
        final Trick trick = newTrickFromPlayer1(players);
        alex.deal(DeckConstants.Dog);
        // jules has no cards, so we'd expect the lead to go to quinn
        quinn.deal(DeckConstants.Star_2);

        final Play.PlayResult play = trick.play(
            alex,
            Collections.singleton(DeckConstants.Dog)
        );
        assertThat(play.result()).isEqualTo(Play.PlayResult.Result.TRICK_END);
        assertThat(trick.currentPlayer()).isEqualTo(quinn);
    }

    @Test
    @Disabled(
        "Currently assuming empty-handed players will 'pass', which is probably not correct ..."
    )
    void playingDogSkipsBackToPlayerIfOthersDone() {}

    private Trick newTrickFromPlayer1(Players players) {
        return new Trick(tichuRules, players.cycleFrom(alex));
    }
}
