package com.sbowling.warmachine.war.batting.impl;

import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.war.batting.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class OnBasePercentage implements BattingMetric {

  @Override
  public String getName() {
    return "On-Base Percentage";
  }

  @Override
  public String getAbbreviation() {
    return "OBP";
  }

  @Override
  public double calculate(PlayerStats stats) {
    int opportunities =
        stats.atBats() + stats.walks() + stats.hitByPitch() + stats.sacrificeFlies();

    if (opportunities == 0) {
      return 0.0;
    }

    return (double) (stats.hits() + stats.walks() + stats.hitByPitch()) / opportunities;
  }

  @Override
  public double getOpportunity(PlayerStats stats) {
    return stats.atBats() + stats.walks() + stats.hitByPitch() + stats.sacrificeFlies();
  }
}
