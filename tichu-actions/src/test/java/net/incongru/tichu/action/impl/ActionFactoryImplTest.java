package net.incongru.tichu.action.impl;

import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActionFactoryImplTest {

    private ActionFactoryImpl actionFactory;

    @BeforeEach
    void setUp() {
        actionFactory = new ActionFactoryImpl();
    }

    @Test
    void throwsOnUnknownActionParam() {
        assertThatThrownBy(() -> actionFactory.actionFor(new FakeActionParam()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("No action for FakeActionParam");
    }

    @Test
    void recognisesInitActionParam() {
        assertThat(actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param()))
                .isInstanceOf(InitialiseGame.class);
    }

    @Test
    void newInstancesOnEachCall() {
        final Action<InitialiseGameParam> first = actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param());
        final Action<InitialiseGameParam> second = actionFactory.actionFor(InitialiseGameParam.withActor(UserId.of("dummy")).param());
        assertThat(first).isNotSameAs(second);
    }

    private static class FakeActionParam implements ActionParam {
    }
}