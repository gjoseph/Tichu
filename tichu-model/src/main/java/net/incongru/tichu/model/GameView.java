package net.incongru.tichu.model;

import java.util.Deque;
import java.util.Set;

/**
 * A view on a current game from a given player's perspective.
 */
public class GameView {
    private final Player you;
    private final Player teamMate;
    private final Team opponents;
    /**
     * The cards in your hand.
     */
    private final Set<Card> hand;

    private final Deque<Play> plays;

    /**
     * Amount of cards collected by the team so far - do we even need this ? Shouldn't be allowed to look at it, but for the sake of showing a small or large pile or cards, maybe ...
     */
    private final int collectedCards;

    public GameView(Player you, Player teamMate, Team opponents, Set<Card> hand, Deque<Play> plays, int collectedCards) {
        this.you = you;
        this.teamMate = teamMate;
        this.opponents = opponents;
        this.hand = hand;
        this.plays = plays;
        this.collectedCards = collectedCards;
    }
}
