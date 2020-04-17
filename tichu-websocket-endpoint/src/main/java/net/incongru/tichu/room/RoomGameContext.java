package net.incongru.tichu.room;

import net.incongru.tichu.action.AbstractGameContext;

public class RoomGameContext extends AbstractGameContext {

    @Override
    public void log(String msg, Object... args) {
        // TODO some of this could be fed back to users?
        //  https://trello.com/c/YSsooILA/18-action-result-should-have-log
        System.out.println(String.format(msg, args));
    }
}
