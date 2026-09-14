package com.sbowling.warmachine.api.mlb.mapper;

import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.league.model.LeagueStats;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.batting.metric.BattingMetricRegistry;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MlbLeagueStatsMapper {

  private final BattingMetricRegistry metricRegistry;

  public MlbLeagueStatsMapper(BattingMetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  public LeagueStats toLeagueStats(MlbStatsResponseDto response) {
    PlayerStats leagueTotals = aggregate(response);

    Map<String, Double> metrics = new HashMap<>();
    for (BattingMetric metric : metricRegistry.getAllMetrics()) {
      metrics.put(metric.getAbbreviation(), metric.calculate(leagueTotals));
    }

    int season = Integer.parseInt(response.stats().getFirst().splits().getFirst().season());
    return new LeagueStats(season, metrics);
  }

  private PlayerStats aggregate(MlbStatsResponseDto response) {
    int gamesPlayed = 0, plateAppearances = 0, atBats = 0, hits = 0, doubles = 0, triples = 0,
            homeRuns = 0, walks = 0, intentionalWalks = 0, hitByPitch = 0, stolenBases = 0,
            caughtStealing = 0, groundIntoDoublePlay = 0, sacrificeBunts = 0, sacrificeFlies = 0,
            runs = 0, rbi = 0;

    for (MlbStatsResponseDto.Split split : response.stats().getFirst().splits()) {
      MlbStatsResponseDto.Stat stat = split.stat();
      gamesPlayed += stat.gamesPlayed();
      plateAppearances += stat.plateAppearances();
      atBats += stat.atBats();
      hits += stat.hits();
      doubles += stat.doubles();
      triples += stat.triples();
      homeRuns += stat.homeRuns();
      walks += stat.baseOnBalls();
      intentionalWalks += stat.intentionalWalks();
      hitByPitch += stat.hitByPitch();
      stolenBases += stat.stolenBases();
      caughtStealing += stat.caughtStealing();
      groundIntoDoublePlay += stat.groundIntoDoublePlay();
      sacrificeBunts += stat.sacBunts();
      sacrificeFlies += stat.sacFlies();
      runs += stat.runs();
      rbi += stat.rbi();
    }

    return new PlayerStats(
            gamesPlayed, plateAppearances, atBats, hits, doubles, triples, homeRuns,
            walks, intentionalWalks, hitByPitch, stolenBases, caughtStealing,
            groundIntoDoublePlay, sacrificeBunts, sacrificeFlies, runs, rbi);
  }
}