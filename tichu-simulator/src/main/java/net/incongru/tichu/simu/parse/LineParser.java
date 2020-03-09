package net.incongru.tichu.simu.parse;

interface LineParser<T> {
    boolean accept(TokenisedLine t);

    T parse(TokenisedLine t);
}
