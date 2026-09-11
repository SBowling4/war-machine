package com.sbowling.warmachine.war.batting;

import com.sbowling.warmachine.model.PlayerStats;

public interface BattingMetric {
  String getName();

  String getAbbreviation();

  double calculate(PlayerStats stats);

  double getOpportunity(PlayerStats stats);
}
