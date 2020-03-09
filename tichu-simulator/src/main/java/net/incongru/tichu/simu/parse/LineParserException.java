package net.incongru.tichu.simu.parse;

class LineParserException extends RuntimeException {
    LineParserException(TokenisedLine line, String message) {
        super("Can't parse line [" + line.whole() + "], " + message + "."); // TODO add possible actions, location and filename
    }
}
