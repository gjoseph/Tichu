package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.impl.PlayerPlays.PlayerPlaysParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static net.incongru.tichu.action.ActionResultAssert.assertThat;
import static net.incongru.tichu.model.Play.PlayResult.Result.INVALIDPLAY;
import static net.incongru.tichu.model.Play.PlayResult.Result.NEXTGOES;
import static net.incongru.tichu.model.Play.PlayResult.Result.NOTINHAND;
import static net.incongru.tichu.model.util.DeckConstants.B2;
import static net.incongru.tichu.model.util.DeckConstants.G9;
import static net.incongru.tichu.model.util.DeckConstants.MahJong;

class PlayerPlaysTest {

    private TestGameContext ctx;

    @BeforeEach
    void setUp() {
        ctx = new TestGameContext().initialised().withSamplePlayers();
    }

    @Test
    @Disabled("https://trello.com/c/RuVtOKVf/51-player-who-has-mahjong-should-not-be-able-to-pass-before-playing-the-first-trick")
    void firstPlayerShouldNotBeAbleToPass() {
        ctx.withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of()).allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(play.exec(ctx, new PlayerPlaysParam("jules", Collections.emptySet()))).isErrorPlayResult(INVALIDPLAY/*??*/);
    }

    @Test
    void playingWrongCardResultsInError() {
        ctx.withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of()).allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(play.exec(ctx, new PlayerPlaysParam("jules", Set.of(G9)))).isErrorPlayResult(NOTINHAND, s -> s.startsWith("You don't have"));
    }

    @Test
    void playMahjongFirst() {
        ctx.withCards(Set.of(), Set.of(), Set.of(MahJong, B2), Set.of()).allReady();

        final PlayerPlays play = new PlayerPlays();
        assertThat(play.exec(ctx, new PlayerPlaysParam("jules", Set.of(MahJong)))).isSuccessPlayResult(NEXTGOES);
    }

    @Test
    void passing() {
        ctx.withCards(Set.of(), Set.of(), Set.of(MahJong), Set.of(B2)).allReady();

        final PlayerPlays play1 = new PlayerPlays();
        assertThat(play1.exec(ctx, new PlayerPlaysParam("jules", Set.of(MahJong)))).isSuccessPlayResult(NEXTGOES);

        final PlayerPlays play2 = new PlayerPlays();
        assertThat(play2.exec(ctx, new PlayerPlaysParam("charlie", Collections.emptySet()))).isSuccessPlayResult(NEXTGOES);
    }

}