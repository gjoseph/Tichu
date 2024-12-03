package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.GameContext;
import net.incongru.tichu.action.param.PlayerPlaysParam;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.Trick;

class PlayerPlays implements Action<PlayerPlaysParam, PlayerPlaysResult> {

    PlayerPlays() {}

    @Override
    public ActionResponse<PlayerPlaysResult> exec(
        GameContext ctx,
        ActionParam.WithActor<PlayerPlaysParam> param
    ) {
        final Player player = ctx.player(param.actor());
        final Trick trick = ctx.game().currentRound().currentTrick();
        final Play.PlayResult playResult = trick.play(
            player,
            param.param().cards()
        );
        // TODO -- this might not be accurate; trick-end doesn't mean there's no next player
        // ... the next player is whoever won the trick
        //
        final boolean isTrickEnd =
            playResult.result() == Play.PlayResult.Result.TRICK_END;
        return new PlayerPlaysResponse(
            param.actor(),
            playResult,
            isTrickEnd ? null : trick.currentPlayer().id(),
            new ActionResponse.Message(playResult.message())
        );
    }
}
