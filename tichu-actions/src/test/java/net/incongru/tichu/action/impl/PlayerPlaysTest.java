package net.incongru.tichu.action.impl;

import static net.incongru.tichu.action.ActionResultAssert.assertThat;
import static net.incongru.tichu.action.impl.PlayerPlaysResult.INVALID_PLAY;
import static net.incongru.tichu.action.impl.PlayerPlaysResult.NEXT_PLAYER_GOES;
import static net.incongru.tichu.action.impl.PlayerPlaysResult.NOT_IN_HAND;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.G9;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;

import java.util.Collections;
import java.util.Set;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PlayerPlaysTest {

    private TestGameContext ctx;

    @BeforeEach
    void setUp() {
        ctx = new TestGameContext().initialised().withSamplePlayers();
    }

    @Test
    @Disabled(
        "https://trello.com/c/RuVtOKVf/51-player-who-has-mahjong-should-not-be-able-to-pass-before-playing-the-first-trick"
    )
    void firstPlayerShouldNotBeAbleToPass() {
        ctx
            .withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of())
            .allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(
            play.exec(
                ctx,
                PlayerPlaysParam.withActor(
                    UserId.of("jules"),
                    Collections.emptySet()
                )
            )
        ).isErrorPlayResult(INVALID_PLAY/* TODO is this the right error ??*/);
    }

    @Test
    void playingWrongCardResultsInError() {
        ctx
            .withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of())
            .allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(
            play.exec(
                ctx,
                PlayerPlaysParam.withActor(UserId.of("jules"), Set.of(G9))
            )
        ).isErrorPlayResult(NOT_IN_HAND, s ->
            s.contains("You don't have those cards")
        );
    }

    @Test
    void playMahjongFirst() {
        ctx
            .withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of())
            .allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(
            play.exec(
                ctx,
                PlayerPlaysParam.withActor(UserId.of("jules"), Set.of(MahJong))
            )
        ).isSuccessPlayResult(NEXT_PLAYER_GOES);
    }

    @Test
    void passing() {
        ctx
            .withCards(Set.of(), Set.of(), Set.of(MahJong), Set.of(B2))
            .allReady();

        final PlayerPlays play1 = new PlayerPlays();
        assertThat(
            play1.exec(
                ctx,
                PlayerPlaysParam.withActor(UserId.of("jules"), Set.of(MahJong))
            )
        ).isSuccessPlayResult(NEXT_PLAYER_GOES);

        final PlayerPlays play2 = new PlayerPlays();
        assertThat(
            play2.exec(
                ctx,
                PlayerPlaysParam.withActor(
                    UserId.of("charlie"),
                    Collections.emptySet()
                )
            )
        ).isSuccessPlayResult(NEXT_PLAYER_GOES);
    }
}
