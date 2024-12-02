package net.incongru.tichu.action.impl;

import java.util.Set;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.NewTrickParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.action.param.PlayerPlaysParam;

public class SimulatedActionFactory extends AbstractActionFactory {

    @Override
    protected Set<Key<?>> build() {
        return Set.of(
            key(CheatDealParam.class, CheatDeal::new),
            key(InitialiseGameParam.class, InitialiseSimulatedGame::new),
            key(JoinTableParam.class, JoinTable::new),
            key(NewTrickParam.class, NewTrick::new),
            key(PlayerIsReadyParam.class, PlayerIsReady::new),
            key(PlayerPlaysParam.class, PlayerPlays::new)
        );
    }
}
