package net.incongru.tichu.room;

public interface RoomProvider {
    // a Room is sort of an extended GameContext (game, new game, players + observers, maybe)

    // also see GameContextProvider

    Room newRoom();

    Room getRoom(String id);
}
