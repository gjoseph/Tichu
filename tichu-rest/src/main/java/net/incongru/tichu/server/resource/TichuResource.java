package net.incongru.tichu.server.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 *
 */
@Path("/game")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TichuResource {

    private final Map<String, RegistratedGame> games;

    public TichuResource() {
        this.games = new LinkedHashMap<>();
    }

    @POST
    @Path("/new")
    // TODO player should not be payload, but some sort of id gotten from session
    public Response newGame(PlayerId firstPlayer) {
        final String uuid = UUID.randomUUID().toString();
        final RegistratedGame game = new RegistratedGame(uuid);
        game.getTable().sit(0, firstPlayer);
        games.put(uuid, game);
        return Response.ok(game).build();
    }

    /**
     * @param gameId
     * @param chair  0-indexed
     * @param player
     */
    @POST
    @Path("/{gameId}/join/{chair}")
    // TODO player should not be payload, but some sort of id gotten from session
    public Response join(
        @PathParam("gameId") String gameId,
        @PathParam("chair") int chair,
        PlayerId player
    ) {
        return getGameAnd(gameId, game -> {
            game.getTable().sit(chair, player);
            return game;
        });
    }

    public Response start() {
        throw new IllegalStateException("not implemented yet");
    }

    @GET
    @Path("/{gameId}")
    public Response view(@PathParam("gameId") String gameId) {
        return getGameAnd(gameId, Function.identity());
    }

    protected Response getGameAnd(
        String gameId,
        Function<RegistratedGame, RegistratedGame> function
    ) {
        final RegistratedGame game = games.get(gameId);
        if (game == null) {
            return notFound();
        }
        final RegistratedGame gamed = function.apply(game);
        return Response.ok(gamed).build();
    }

    protected Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
