package net.incongru.tichu.model;

/**
 * A round is a series of {@link Trick}s leading to the playing of all cards and scoring.
 */
public class Round {
    private final TichuRules rules;
    private Announce announcePlayer1, announcePlayer2, announcePlayer3, announcePlayer4;

    public Round(TichuRules rules, CardDeck deck) {
        this.rules = rules;
        throw new IllegalStateException("not implemented yet"); // TODO
    }

    public Trick start() {
        // shuffle and deal cards

        final Trick trick = new Trick(rules, null);
        return trick;
    }

    public void endWith(Trick trick) {
    }
}
