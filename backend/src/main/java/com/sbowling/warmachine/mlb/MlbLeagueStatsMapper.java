package com.sbowling.warmachine.mlb;

import com.sbowling.warmachine.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.model.LeagueStats;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MlbLeagueStatsMapper {

  public LeagueStats toLeagueStats(MlbStatsResponseDto response) {
    int totalHits = 0;
    int totalAtBats = 0;
    int totalWalks = 0;
    int totalHitByPitch = 0;
    int totalSacrificeFlies = 0;
    int totalBases = 0;

    for (MlbStatsResponseDto.Split split : response.stats().getFirst().splits()) {
      MlbStatsResponseDto.Stat stat = split.stat();

      totalHits += stat.hits();
      totalAtBats += stat.atBats();
      totalWalks += stat.baseOnBalls();
      totalHitByPitch += stat.hitByPitch();
      totalSacrificeFlies += stat.sacFlies();
      totalBases += stat.totalBases();
    }

    double battingAverage = (double) totalHits / totalAtBats;

    double onBasePercentage =
        (double) (totalHits + totalWalks + totalHitByPitch)
            / (totalAtBats + totalWalks + totalHitByPitch + totalSacrificeFlies);

    double sluggingPercentage = (double) totalBases / totalAtBats;

    Map<String, Double> metrics =
        Map.of(
            "AVG", battingAverage,
            "OBP", onBasePercentage,
            "SLG", sluggingPercentage);

    int season = Integer.parseInt(response.stats().getFirst().splits().getFirst().season());

    return new LeagueStats(season, metrics);
  }
}
