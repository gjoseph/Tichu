package net.incongru.tichu.simu.cmd;

import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.TichuRules;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static net.incongru.tichu.model.util.DeckConstants.G2;
import static net.incongru.tichu.model.util.DeckConstants.G5;
import static net.incongru.tichu.model.util.DeckConstants.K2;
import static net.incongru.tichu.model.util.DeckConstants.R2;
import static net.incongru.tichu.model.util.DeckConstants.R3;
import static net.incongru.tichu.model.util.DeckConstants.R4;
import static net.incongru.tichu.model.util.DeckConstants.R5;
import static net.incongru.tichu.model.util.DeckConstants.R6;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpectablePlayTest {
    @Test
    public void enumPredicateOnStraightBomb() {
        final PostActionCommandFactory.ExpectablePlay straightBomb = PostActionCommandFactory.ExpectablePlay.StraightBomb;
        assertTrue(straightBomb.test(getPlay(R2, R3, R4, R5, R6)));
        assertFalse(straightBomb.test(getPlay(R2, R3, R4, G5, R6)));
        assertFalse(straightBomb.test(getPlay(R2, R3, R4, R5)));
    }

    @Test
    public void classNameBasedEnumPredicate() {
        final PostActionCommandFactory.ExpectablePlay pair = PostActionCommandFactory.ExpectablePlay.Pair;
        assertTrue(pair.test(getPlay(R2, G2)));
        assertFalse(pair.test(getPlay(R2, G2, K2)));
        assertFalse(pair.test(getPlay(R2, R3)));

    }

    private Play getPlay(Card... cards) {
        return new TichuRules().validate(Set.of(cards));
    }

}