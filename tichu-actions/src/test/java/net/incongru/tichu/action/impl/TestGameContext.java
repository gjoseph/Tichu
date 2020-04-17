package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.AbstractGameContext;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.action.param.JoinTableParam;
import net.incongru.tichu.action.param.PlayerIsReadyParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.UserId;

import java.util.Set;

class TestGameContext extends AbstractGameContext {
    TestGameContext initialised() {
        new InitialiseGame().exec(this, InitialiseGameParam.withActor(UserId.of("alex")));
        return this;
    }

    TestGameContext withSamplePlayers() {
        // play order would be alex jules charlie quinn
        new JoinTable().exec(this, JoinTableParam.withActor(UserId.of("alex"), 0)); // player 1 (team 1)
        new JoinTable().exec(this, JoinTableParam.withActor(UserId.of("charlie"), 0));  // player 3 (team 1)
        new JoinTable().exec(this, JoinTableParam.withActor(UserId.of("jules"), 1));  // player 2 (team 2)
        new JoinTable().exec(this, JoinTableParam.withActor(UserId.of("quinn"), 1)); // player 4 (team 2)
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext withCards(Set<Card> forAlex, Set<Card> forCharlie, Set<Card> forJules, Set<Card> forQuinn) {
        new CheatDeal().exec(this, CheatDealParam.withActor(UserId.of("alex"), forAlex));
        new CheatDeal().exec(this, CheatDealParam.withActor(UserId.of("charlie"), forCharlie));
        new CheatDeal().exec(this, CheatDealParam.withActor(UserId.of("jules"), forJules));
        new CheatDeal().exec(this, CheatDealParam.withActor(UserId.of("quinn"), forQuinn));
        return this;
    }

    // ideally shouldn't be able to call this if haven't set sample players...
    TestGameContext allReady() {
        new PlayerIsReady().exec(this, PlayerIsReadyParam.withActor(UserId.of("alex")));
        new PlayerIsReady().exec(this, PlayerIsReadyParam.withActor(UserId.of("charlie")));
        new PlayerIsReady().exec(this, PlayerIsReadyParam.withActor(UserId.of("jules")));
        new PlayerIsReady().exec(this, PlayerIsReadyParam.withActor(UserId.of("quinn")));
        return this;
    }

    TestGameContext() {
    }

    @Override
    public void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
