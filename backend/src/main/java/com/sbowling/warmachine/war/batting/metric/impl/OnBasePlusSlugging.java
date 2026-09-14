package com.sbowling.warmachine.war.batting.metric.impl;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class OnBasePlusSlugging implements BattingMetric {
  @Override
  public String getName() {
    return "On Base Plus Slugging";
  }

  @Override
  public String getAbbreviation() {
    return "OPS";
  }

  @Override
  public double calculate(PlayerStats stats) {
    int obpOpportunities =
        stats.atBats() + stats.walks() + stats.hitByPitch() + stats.sacrificeFlies();

    if (obpOpportunities == 0) {
      return 0.0;
    }

    double obp = (double) (stats.hits() + stats.walks() + stats.hitByPitch()) / obpOpportunities;

    int singles = stats.hits() - stats.doubles() - stats.triples() - stats.homeRuns();
    double slugging =
        (double) (singles + (2 * stats.doubles()) + (3 * stats.triples()) + (4 * stats.homeRuns()))
            / stats.atBats();

    return obp + slugging;
  }

  @Override
  public double getOpportunity(PlayerStats stats) {
    return stats.plateAppearances();
  }
}
