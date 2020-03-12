package net.incongru.tichu.action;

import net.incongru.tichu.action.ActionResult.AbstractPlayResult;
import net.incongru.tichu.action.ActionResult.Error;
import net.incongru.tichu.action.ActionResult.ErrorPlayResult;
import net.incongru.tichu.action.ActionResult.Success;
import net.incongru.tichu.action.ActionResult.SuccessPlayResult;
import net.incongru.tichu.model.Play;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionResultAssert extends AbstractAssert<ActionResultAssert, ActionResult> {
    public static ActionResultAssert assertThat(ActionResult actual) {
        return new ActionResultAssert(actual);
    }

    public ActionResultAssert(ActionResult actual) {
        super(actual, ActionResultAssert.class);
    }

    public ActionResultAssert isSuccessPlayResult(Play.PlayResult.Result expectedResultType) {
        isNotNull();
        // not producing great stack trace (starts in the assert methods) or errors (bc PlayResult doesnt have a toString)
        return isInstanceOfSatisfying(SuccessPlayResult.class, hasResultType(expectedResultType));
    }

    public ActionResultAssert isErrorPlayResult(Play.PlayResult.Result expectedResultType) {
        return isErrorPlayResult(expectedResultType, s -> true);
    }

    public ActionResultAssert isErrorPlayResult(Play.PlayResult.Result expectedResultType, Predicate<String> errorMessagePredicate) {
        isNotNull();
        return isInstanceOfSatisfying(ErrorPlayResult.class, hasResultType(expectedResultType));
    }

    public ActionResultAssert isSuccessResult() {
        isNotNull();
        return isInstanceOf(Success.class);
    }

    public ActionResultAssert isErrorResult(Predicate<String> errorMessagePredicate) {
        isNotNull();
        return isInstanceOf(Error.class).satisfies(r -> {
            final String error = ((Error) r).error();
            Assertions.assertThat(error).matches(errorMessagePredicate);
        });
    }

    private <T extends AbstractPlayResult> Consumer<T> hasResultType(Play.PlayResult.Result expectedResultType) {
        return r -> Assertions.assertThat(r.playResult().result()).isEqualTo(expectedResultType);
    }
}
