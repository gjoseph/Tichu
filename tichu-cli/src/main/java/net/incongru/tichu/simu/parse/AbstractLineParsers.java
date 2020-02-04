package net.incongru.tichu.simu.parse;

import java.util.List;

class AbstractLineParsers<T> {
    private final List<LineParser<T>> parsers;
    private final String lineType;

    AbstractLineParsers(String lineType, List<LineParser<T>> parsers) {
        this.lineType = lineType;
        this.parsers = parsers;
    }

    T parse(String line) {
        final TokenisedLine tokens = new TokenisedLine(line);
        return parsers.stream()
                .filter(p -> p.accept(tokens)) // this does not reset the state of the line, so we expect parsers to be careful
                .findFirst()
                .map(p -> p.parse(tokens))
                .orElseThrow(() -> new LineParserException(tokens, "unrecognised " + lineType));
    }
}
