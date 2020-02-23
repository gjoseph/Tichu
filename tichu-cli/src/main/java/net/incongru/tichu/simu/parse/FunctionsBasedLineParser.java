package net.incongru.tichu.simu.parse;

import java.util.function.Function;
import java.util.function.Predicate;

class FunctionsBasedLineParser<T> implements LineParser<T> {

    static <T> LineParser<T> simpleParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, T> function) {
        return new FunctionsBasedLineParser<T>(predicate, function);
    }

    private final Predicate<TokenisedLine> predicate;
    private final Function<TokenisedLine, T> function;

    FunctionsBasedLineParser(Predicate<TokenisedLine> predicate, Function<TokenisedLine, T> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public boolean accept(TokenisedLine t) {
        return predicate.test(t);
    }

    public T parse(TokenisedLine t) {
        return function.apply(t);
    }

}
