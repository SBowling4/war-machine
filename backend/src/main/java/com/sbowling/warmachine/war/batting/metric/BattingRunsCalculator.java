package com.sbowling.warmachine.war.batting.metric;

import com.sbowling.warmachine.league.model.LeagueStats;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.player.model.PlayerStats;
import org.springframework.stereotype.Component;

@Component
public class BattingRunsCalculator {

  public double calculate(
      PlayerStats player, LeagueStats league, BattingMetric metric, MetricCalibration calibration) {
    double playerValue = metric.calculate(player);
    double leagueValue = league.getValue(metric.getAbbreviation());
    double opportunity = metric.getOpportunity(player);

    return ((playerValue - leagueValue) / calibration.getScale()) * opportunity;
  }
}
