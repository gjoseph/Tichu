package net.incongru.tichu.simu.parse;

import java.util.List;

class AbstractLineParsers<T> {
    private final List<LineParser<T>> parsers;
    private final String lineType;

    AbstractLineParsers(String lineType, List<LineParser<T>> parsers) {
        this.lineType = lineType;
        this.parsers = parsers;
    }

    T parse(TokenisedLine line) {
        return parsers.stream()
                .filter(p -> p.accept(line)) // this does not reset the state of the line, so we expect parsers to be careful
                .findFirst()
                .map(p -> p.parse(line))
                .orElseThrow(() -> new LineParserException(line, "unrecognised " + lineType));
    }
}
