package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.Action;

import java.util.function.Function;
import java.util.function.Predicate;

class FunctionsBasedActionLineParser implements ActionLineParser {

    static ActionLineParser simpleParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, Action> function) {
        return new FunctionsBasedActionLineParser(predicate, function);
    }

    private final Predicate<TokenisedLine> predicate;
    private final Function<TokenisedLine, Action> function;

    FunctionsBasedActionLineParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, Action> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public boolean accept(TokenisedLine t) {
        return predicate.test(t);
    }

    public Action parse(TokenisedLine t) {
        return function.apply(t);
    }

}
