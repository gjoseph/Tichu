package net.incongru.tichu.room;

import java.util.LinkedList;
import java.util.List;
import net.incongru.tichu.model.UserId;

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
