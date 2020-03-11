package net.incongru.tichu.server.resource;

import net.incongru.tichu.model.CardDeck;
import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.TichuRules;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("/game")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TichuResource {

    // TODO expose rest-ian resources instead of the underlying model
    // (i.e to not expose other player's cards)

    @GET
    @Path("/new")
    public CardDeck newDeck() {
        return new CardDeck();
    }

    @GET
    @Path("/start")
    public Game newGame() {
        return new Game(new Players(), new TichuRules());
    }

    //    public Game.PlayResult validate(Game.Play play) {
    //        return null;
    //    }
}
