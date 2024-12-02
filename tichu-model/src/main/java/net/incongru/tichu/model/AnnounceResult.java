package net.incongru.tichu.model;

/**
 * After play, the announce was met or failed.
 */
record AnnounceResult(Player player, Announce announce, Boolean result) {}
