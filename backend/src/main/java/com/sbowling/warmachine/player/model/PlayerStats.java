package com.sbowling.warmachine.player.model;

public record PlayerStats(
    int gamesPlayed,
    int plateAppearances,
    int atBats,
    int hits,
    int doubles,
    int triples,
    int homeRuns,
    int walks,
    int intentionalWalks,
    int hitByPitch,
    int stolenBases,
    int caughtStealing,
    int groundIntoDoublePlay,
    int sacrificeBunts,
    int sacrificeFlies,
    int runs,
    int rbi) {}
