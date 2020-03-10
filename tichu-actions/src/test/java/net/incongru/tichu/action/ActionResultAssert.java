package net.incongru.tichu.action;

import net.incongru.tichu.model.Play;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionResultAssert extends AbstractAssert<ActionResultAssert, Action.Result> {
    public static ActionResultAssert assertThat(Action.Result actual) {
        return new ActionResultAssert(actual);
    }

    public ActionResultAssert(Action.Result actual) {
        super(actual, ActionResultAssert.class);
    }

    public ActionResultAssert isSuccessPlayResult(Play.PlayResult.Result expectedResultType) {
        isNotNull();
        // not producing great stack trace (starts in the assert methods) or errors (bc PlayResult doesnt have a toString)
        return isInstanceOfSatisfying(Action.SuccessPlayResult.class, hasResultType(expectedResultType));
    }

    public ActionResultAssert isErrorPlayResult(Play.PlayResult.Result expectedResultType) {
        return isErrorPlayResult(expectedResultType, s -> true);
    }

    public ActionResultAssert isErrorPlayResult(Play.PlayResult.Result expectedResultType, Predicate<String> errorMessagePredicate) {
        isNotNull();
        return isInstanceOfSatisfying(Action.ErrorPlayResult.class, hasResultType(expectedResultType));
    }

    public ActionResultAssert isSuccessResult() {
        isNotNull();
        return isInstanceOf(Action.Success.class);
    }

    public ActionResultAssert isErrorResult(Predicate<String> errorMessagePredicate) {
        isNotNull();
        return isInstanceOf(Action.Error.class).satisfies(r -> {
            final String error = ((Action.Error) r).error();
            Assertions.assertThat(error).matches(errorMessagePredicate);
        });
    }

    private <T extends Action.AbstractPlayResult> Consumer<T> hasResultType(Play.PlayResult.Result expectedResultType) {
        return r -> Assertions.assertThat(r.playResult().result()).isEqualTo(expectedResultType);
    }
}
