package net.incongru.tichu.action;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.incongru.tichu.action.impl.PlayerPlaysResponse;
import net.incongru.tichu.action.impl.PlayerPlaysResult;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ActionResultAssert
    extends AbstractAssert<ActionResultAssert, ActionResponse> {

    public static ActionResultAssert assertThat(ActionResponse actual) {
        return new ActionResultAssert(actual);
    }

    public ActionResultAssert(ActionResponse actual) {
        super(actual, ActionResultAssert.class);
    }

    public ActionResultAssert isSuccessPlayResult(
        PlayerPlaysResult expectedResultType
    ) {
        isNotNull();
        // not producing great stack trace (starts in the assert methods) or errors (bc PlayResult doesnt have a toString)
        return isInstanceOf(PlayerPlaysResponse.class)
            .satisfies(isSuccessful(true))
            .satisfies(hasResultType(expectedResultType));
    }

    public ActionResultAssert isErrorPlayResult(
        PlayerPlaysResult expectedResultType
    ) {
        return isErrorPlayResult(expectedResultType, s -> true);
    }

    public ActionResultAssert isErrorPlayResult(
        PlayerPlaysResult expectedResultType,
        Predicate<String> errorMessagePredicate
    ) {
        isNotNull();
        return isInstanceOf(PlayerPlaysResponse.class)
            .satisfies(isSuccessful(false))
            .satisfies(hasResultType(expectedResultType))
            .satisfies(hasMessage(errorMessagePredicate));
    }

    public ActionResultAssert isSuccessResult() {
        isNotNull();
        return satisfies(isSuccessful(true));
    }

    public ActionResultAssert isErrorResult(
        Predicate<String> errorMessagePredicate
    ) {
        isNotNull();
        // TODO maybe we want to differentiate error/fail
        return satisfies(isSuccessful(false)).satisfies(
            hasMessage(errorMessagePredicate)
        );
    }

    private Consumer<ActionResponse> isSuccessful(boolean expectedSuccessful) {
        return r ->
            Assertions.assertThat(r.result().isSuccessful())
                .describedAs(
                    "Result for %s is %s, expecting %s",
                    r.forAction(),
                    r.result(),
                    expectedSuccessful ? "success" : "failure"
                )
                .isEqualTo(expectedSuccessful);
    }

    private <ARR extends ActionResponse.Result> Consumer<
        ActionResponse
    > hasResultType(ARR expectedResultType) {
        // Using equalTo since we expect all impls of Result to be enums
        return r ->
            Assertions.assertThat(r.result()).isEqualTo(expectedResultType);
    }

    private Consumer<ActionResponse> hasMessage(
        Predicate<String> messagePredicate
    ) {
        // TODO we're asserting on toString of Message, which is not great
        return r ->
            Assertions.assertThat(r.message().toString()).matches(
                messagePredicate
            );
    }
}
