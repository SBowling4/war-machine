package com.sbowling.warmachine.war.batting.metric;

import com.sbowling.warmachine.player.model.PlayerStats;

public interface BattingMetric {
  String getName();

  String getAbbreviation();

  double calculate(PlayerStats stats);

  double getOpportunity(PlayerStats stats);
}
