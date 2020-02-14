package net.incongru.tichu.assertj;

import net.incongru.tichu.model.Game;
import org.assertj.core.api.Condition;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.not;

public class Conditions {
    public static final Condition<Game> ready = new Condition<>(Game::isReadyToStart, "ready");
    public static final Condition<Game> started = new Condition<>(Game::isStarted, "started");
    public static final Condition<Game> notReadyNorStarted = allOf(not(ready), not(started));
    // There is a bug in Not that does not propagate the Description (it overrides toString only),
    // so when used in combination with allOf(), the description of the nested condition is lost:
    // allOf: [Not, ...]
    // TODO Contribute a fix with tests here https://github.com/joel-costigliola/assertj-core/tree/master/src/test/java/org/assertj/core/condition
}
