package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.NewTrickParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;

import java.util.Set;

public class DefaultActionFactory extends AbstractActionFactory {
    @Override
    protected Set<Key<?>> build() {
        return Set.of(
                key(InitialiseGameParam.class, InitialiseGame::new),
                key(JoinTableParam.class, JoinTable::new),
                key(NewTrickParam.class, NewTrick::new),
                key(PlayerIsReadyParam.class, PlayerIsReady::new),
                key(PlayerPlaysParam.class, PlayerPlays::new)
        );
    }
}
