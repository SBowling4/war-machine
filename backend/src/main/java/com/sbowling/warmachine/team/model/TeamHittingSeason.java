package com.sbowling.warmachine.team.model;

import com.sbowling.warmachine.player.model.PlayerStats;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "team_hitting_seasons")
public class TeamHittingSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int season;
    private int teamId;
    private String teamName;
    private int gamesPlayed;
    private int plateAppearances;
    private int atBats;
    private int hits;
    private int doubles;
    private int triples;
    private int homeRuns;
    private int walks;
    private int intentionalWalks;
    private int hitByPitch;
    private int stolenBases;
    private int caughtStealing;
    private int groundIntoDoublePlay;
    private int sacrificeBunts;
    private int sacrificeFlies;
    private int runs;
    private int rbi;

    protected TeamHittingSeason() {}

    public TeamHittingSeason(int season, int teamId, String teamName, PlayerStats stats) {
        this.season = season;
        this.teamId = teamId;
        this.teamName = teamName;
        this.gamesPlayed = stats.gamesPlayed();
        this.plateAppearances = stats.plateAppearances();
        this.atBats = stats.atBats();
        this.hits = stats.hits();
        this.doubles = stats.doubles();
        this.triples = stats.triples();
        this.homeRuns = stats.homeRuns();
        this.walks = stats.walks();
        this.intentionalWalks = stats.intentionalWalks();
        this.hitByPitch = stats.hitByPitch();
        this.stolenBases = stats.stolenBases();
        this.caughtStealing = stats.caughtStealing();
        this.groundIntoDoublePlay = stats.groundIntoDoublePlay();
        this.sacrificeBunts = stats.sacrificeBunts();
        this.sacrificeFlies = stats.sacrificeFlies();
        this.runs = stats.runs();
        this.rbi = stats.rbi();
    }

    public PlayerStats toPlayerStats() {
        return new PlayerStats(
                gamesPlayed, plateAppearances, atBats, hits, doubles, triples, homeRuns,
                walks, intentionalWalks, hitByPitch, stolenBases, caughtStealing,
                groundIntoDoublePlay, sacrificeBunts, sacrificeFlies, runs, rbi);
    }

    public int getSeason() { return season; }
    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
}