package com.sbowling.warmachine.war.batting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sbowling.warmachine.league.model.LeagueStats;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.batting.metric.BattingRunsCalculator;
import com.sbowling.warmachine.war.batting.metric.impl.BattingAverage;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BattingRunsCalculatorTest {

  private final BattingRunsCalculator calculator = new BattingRunsCalculator();

  @Test
  void calculatesBattingRuns() {
    PlayerStats player = new PlayerStats(100, 600, 600, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    LeagueStats league = new LeagueStats(2025, Map.of("AVG", 0.250));

    MetricCalibration calibration = new MetricCalibration(2025, "AVG", 1.25);

    BattingMetric metric = new BattingAverage();

    double result = calculator.calculate(player, league, metric, calibration);

    assertEquals(24.0, result, 0.000001);
  }
}
