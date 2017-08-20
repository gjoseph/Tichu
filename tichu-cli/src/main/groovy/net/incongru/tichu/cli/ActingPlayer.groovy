package net.incongru.tichu.cli

import net.incongru.tichu.model.Card
import net.incongru.tichu.model.Players

public class ActingPlayer {
    private final TichuDSL ctx
    private final Players.Player p

    ActingPlayer(TichuDSL ctx, Players.Player p) {
        this.ctx = ctx
        this.p = p
    }

    def plays(Card... cards) {
        return ctx.playerPlayCards(p, cards)
    }

//        [
//                plays : plays(p),
//                passes: {
    //TODO is it even their turn ?
//                    ui().println "> ${p.name} passes."
//                }
//        ]

    @Override
    def getProperty(String property) {
        Optional<Object> card = DeckConstants.byName(property)
        return card.orElseGet({ super.getProperty(property) })
        // We may want to just stop explicitly for anything that's not a known card?
    }
}
