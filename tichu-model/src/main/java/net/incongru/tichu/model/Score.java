package net.incongru.tichu.model;

public record Score(int team1, int team2) {
    @Override
    public String toString() {
        return "%d:%d".formatted(team1(), team2());
    }
}
