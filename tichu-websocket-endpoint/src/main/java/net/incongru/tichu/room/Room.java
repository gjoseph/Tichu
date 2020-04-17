package net.incongru.tichu.room;

import net.incongru.tichu.model.UserId;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private final RoomGameContext ctx;
    private final List<UserId> users;

    public Room(RoomGameContext roomGameContext) {
        ctx = roomGameContext;
        users = new LinkedList<>();
    }

    public RoomGameContext gameContext() {
        return ctx;
    }

    public void enter(UserId user) {
        users.add(user);
    }
}
