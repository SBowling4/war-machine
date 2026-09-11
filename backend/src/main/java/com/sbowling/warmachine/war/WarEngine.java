package com.sbowling.warmachine.war;

import com.sbowling.warmachine.model.LeagueStats;
import com.sbowling.warmachine.model.MetricCalibration;
import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.model.WarResult;
import com.sbowling.warmachine.war.batting.BattingMetric;
import com.sbowling.warmachine.war.batting.BattingRunsCalculator;
import org.springframework.stereotype.Component;

@Component
public class WarEngine {
    private final BattingRunsCalculator battingRunsCalculator;
    private final PrototypeWarConfig config;


    public WarEngine(BattingRunsCalculator battingRunsCalculator, PrototypeWarConfig config) {
        this.battingRunsCalculator = battingRunsCalculator;
        this.config = config;
    }

    public WarResult calculate(PlayerStats player, LeagueStats league, BattingMetric metric, MetricCalibration calibration) {
        double battingRuns = battingRunsCalculator.calculate(player, league, metric, calibration);
        double baserunningRuns = 0.0; // Placeholder for baserunning runs calculation
        double fieldingRuns = 0.0; // Placeholder for fielding runs calculation
        double replacementRuns = player.gamesPlayed() * config.getReplacementRunsPerGame();

        double runsAboveReplacement = battingRuns + baserunningRuns + fieldingRuns + replacementRuns;
        double war = runsAboveReplacement / config.getRunsPerWin();

        return new WarResult(battingRuns, baserunningRuns, fieldingRuns, replacementRuns, runsAboveReplacement, war);
    }
}
