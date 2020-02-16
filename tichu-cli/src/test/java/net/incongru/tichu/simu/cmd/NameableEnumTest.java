package net.incongru.tichu.simu.cmd;

import org.junit.jupiter.api.Test;

import static net.incongru.tichu.simu.cmd.PostActionCommandFactory.NameableEnum.byName;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NameableEnumTest {

    @Test
    void supportsAlternativeNamesForEnums() {
        // TODO write test with own enum
        assertEquals(PostActionCommandFactory.TemporaryPlayNamesEnum.Straight, byName(PostActionCommandFactory.TemporaryPlayNamesEnum.class, "straiGHT"));
        assertEquals(PostActionCommandFactory.TemporaryPlayNamesEnum.Straight, byName(PostActionCommandFactory.TemporaryPlayNamesEnum.class, "strEeT"));
        assertEquals(PostActionCommandFactory.TemporaryPlayNamesEnum.Pair, byName(PostActionCommandFactory.TemporaryPlayNamesEnum.class, "Pair"));
        assertEquals(PostActionCommandFactory.ExpectableGameState.Started, byName(PostActionCommandFactory.ExpectableGameState.class, "started"));
        assertEquals(PostActionCommandFactory.ExpectableGameState.NotStarted, byName(PostActionCommandFactory.ExpectableGameState.class, "not staRtEd"));
    }
}
