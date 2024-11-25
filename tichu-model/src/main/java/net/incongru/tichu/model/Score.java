package net.incongru.tichu.model;

public record Score(int team1, int team2) {

    @Override
    public String toString() {
        return String.format("%d:%d", team1(), team2());
    }
}
