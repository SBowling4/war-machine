package com.sbowling.warmachine.war.batting.metric.impl;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class SluggingPercentage implements BattingMetric {

  @Override
  public String getName() {
    return "Slugging Percentage";
  }

  @Override
  public String getAbbreviation() {
    return "SLG";
  }

  @Override
  public double calculate(PlayerStats stats) {
    if (stats.atBats() == 0) return 0.0;
    int singles = stats.hits() - stats.doubles() - stats.triples() - stats.homeRuns();
    return (double)
            (singles + (2 * stats.doubles()) + (3 * stats.triples()) + (4 * stats.homeRuns()))
        / stats.atBats();
  }

  @Override
  public double getOpportunity(PlayerStats stats) {
    return stats.atBats();
  }
}
