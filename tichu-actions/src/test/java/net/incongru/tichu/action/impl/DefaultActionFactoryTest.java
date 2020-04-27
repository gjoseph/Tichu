package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionFactory;
import net.incongru.tichu.action.param.ImmutableCheatDealParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.util.DeckConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultActionFactoryTest {

    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        actionFactory = new DefaultActionFactory();
    }

    @Test
    void throwsOnCheatDealActionParam() {
        assertThatThrownBy(() -> actionFactory.actionFor(ImmutableCheatDealParam.builder().addCards(DeckConstants.MahJong).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("No action for ImmutableCheatDealParam");
    }

    @Test
    void initActionIsRealGame() {
        assertThat(actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param()))
                .isInstanceOf(InitialiseGame.class)
                .isNotInstanceOf(InitialiseSimulatedGame.class);
    }

    @Test
    void newInstancesOnEachCall() {
        final Action<InitialiseGameParam, ?> first = actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param());
        final Action<InitialiseGameParam, ?> second = actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param());
        assertThat(first).isNotSameAs(second);
    }
}