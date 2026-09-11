package com.sbowling.warmachine.war;

import com.sbowling.warmachine.league.model.LeagueStats;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.model.WarResult;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.batting.metric.BattingRunsCalculator;
import com.sbowling.warmachine.war.batting.metric.impl.BattingAverage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarEngineTest {
    private final WarEngine engine = new WarEngine(new BattingRunsCalculator(), new PrototypeWarConfig());

    @Test
    void calculatesWar() {
        PlayerStats player =
                new PlayerStats(
                        100, 600, 600, 180,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                );

        LeagueStats league =
                new LeagueStats(
                        2025,
                        Map.of("AVG", 0.250)
                );

        MetricCalibration calibration =
                new MetricCalibration(
                        2025,
                        "AVG",
                        1.25
                );

        BattingMetric metric = new BattingAverage();

        WarResult result =
                engine.calculate(
                        player,
                        league,
                        metric,
                        calibration
                );

        assertEquals(24.0, result.battingRuns(), 0.000001);
        assertEquals(0.0, result.baserunningRuns(), 0.000001);
        assertEquals(0.0, result.fieldingRuns(), 0.000001);

        assertEquals(
                100 * 0.294,
                result.replacementRuns(),
                0.000001
        );

        assertEquals(
                24.0 + (100 * 0.294),
                result.runsAboveReplacement(),
                0.000001
        );

        assertEquals(
                (24.0 + (100 * 0.294)) / 10.0,
                result.war(),
                0.000001
        );
    }
}
