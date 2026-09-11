package com.sbowling.warmachine.service;

import com.sbowling.warmachine.model.LeagueStats;
import com.sbowling.warmachine.model.MetricCalibration;
import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.war.batting.BattingMetric;
import com.sbowling.warmachine.war.batting.BattingMetricRegistry;
import com.sbowling.warmachine.war.batting.BattingRunsCalculator;
import com.sbowling.warmachine.player.service.PlayerService;
import org.springframework.stereotype.Service;


@Service
public class BattingService {
    private final PlayerService playerService;
    private  final LeagueService leagueService;
    private final BattingMetricRegistry metricRegistry;
    private final BattingRunsCalculator battingRunsCalculator;

    public BattingService(PlayerService playerService, LeagueService leagueService, BattingMetricRegistry metricRegistry, BattingRunsCalculator battingRunsCalculator) {
        this.playerService = playerService;
        this.leagueService = leagueService;
        this.metricRegistry = metricRegistry;
        this.battingRunsCalculator = battingRunsCalculator;
    }

    public double calculateBattingRuns(int playerId, int season, String metricAbbreviation, MetricCalibration calibration) {
        PlayerStats playerStats = playerService.getPlayerStats(playerId, season);

        LeagueStats leagueStats = leagueService.getLeagueStats(season);

        BattingMetric metric = metricRegistry.getMetric(metricAbbreviation);

        return battingRunsCalculator.calculate(playerStats, leagueStats, metric, calibration);
    }
}
