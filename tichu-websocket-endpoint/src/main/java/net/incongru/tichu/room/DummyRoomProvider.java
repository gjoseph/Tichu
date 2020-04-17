package net.incongru.tichu.room;

import net.incongru.tichu.model.Game;
import net.incongru.tichu.model.Players;
import net.incongru.tichu.model.Team;
import net.incongru.tichu.model.TichuRules;

/**
 * Fake room provider that just has a single room.
 */
public class DummyRoomProvider implements RoomProvider {
    private final Room room;

    public DummyRoomProvider() {
        // room should support multiple games i.e renew GameContext
        this.room = new Room(new RoomGameContext());

        // Normally done by init action:
        room.gameContext().newGame(new Game(new Players(), new TichuRules()));
        room.gameContext().game().players().add(new Team("One"));
        room.gameContext().game().players().add(new Team("Two"));


        // for real ones:
        // GameContextFactory ctxFactory = RoomGameContext::new;
    }

    @Override
    public Room newRoom() {
        throw new IllegalStateException("This RoomProvider only supports a single room");
    }

    @Override
    public Room getRoom(String id) {
        return this.room;
    }
}
