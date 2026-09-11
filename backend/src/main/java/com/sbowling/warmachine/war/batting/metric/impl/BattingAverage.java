package com.sbowling.warmachine.war.batting.metric.impl;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class BattingAverage implements BattingMetric {
  @Override
  public String getName() {
    return "Batting Average";
  }

  @Override
  public String getAbbreviation() {
    return "AVG";
  }

  @Override
  public double calculate(PlayerStats stats) {
    if (stats.plateAppearances() == 0) return 0.0;

    return (double) stats.hits() / stats.plateAppearances();
  }

  @Override
  public double getOpportunity(PlayerStats stats) {
    return stats.atBats();
  }
}
