package net.incongru.tichu.simu.parse;

import net.incongru.tichu.action.Action;

interface ActionLineParser {
    boolean accept(TokenisedLine t);

    Action parse(TokenisedLine t);
}
