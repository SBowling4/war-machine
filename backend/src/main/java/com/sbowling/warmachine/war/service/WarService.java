package com.sbowling.warmachine.war.service;

import com.sbowling.warmachine.league.model.LeagueStats;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.model.WarResult;
import com.sbowling.warmachine.player.service.PlayerService;
import com.sbowling.warmachine.league.service.LeagueService;
import com.sbowling.warmachine.war.WarEngine;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.batting.metric.BattingMetricRegistry;
import com.sbowling.warmachine.war.dto.WarResponseDto;
import com.sbowling.warmachine.war.mapper.WarResultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarService {

    private final PlayerService playerService;
    private final LeagueService leagueService;
    private final BattingMetricRegistry metricRegistry;
    private final WarEngine warEngine;
    private final WarResultMapper warResultMapper;

    public WarService(
            PlayerService playerService,
            LeagueService leagueService,
            BattingMetricRegistry metricRegistry,
            WarEngine warEngine,
            WarResultMapper warResultMapper
    ) {
        this.playerService = playerService;
        this.leagueService = leagueService;
        this.metricRegistry = metricRegistry;
        this.warEngine = warEngine;
        this.warResultMapper = warResultMapper;
    }

    public WarResponseDto calculateWar(
            int playerId,
            int season,
            String metricAbbreviation
    ) {
        PlayerStats playerStats =
                playerService.getPlayerStats(playerId, season);

        LeagueStats leagueStats =
                leagueService.getLeagueStats(season);

        BattingMetric battingMetric =
                metricRegistry.getMetric(metricAbbreviation);

        MetricCalibration calibration =
                new MetricCalibration(
                        season,
                        battingMetric.getAbbreviation(),
                        1.25
                );

        WarResult result =
                warEngine.calculate(
                        playerStats,
                        leagueStats,
                        battingMetric,
                        calibration
                );

        return warResultMapper.toDto(result, playerId, season, battingMetric.getAbbreviation());
    }

    public List<WarResponseDto> compareMetrics(int playerId, int season) {
        return metricRegistry.getAllMetrics().stream()
                .map(metric -> calculateWar(playerId, season, metric.getAbbreviation()))
                .toList();
    }
}