package com.sbowling.warmachine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sbowling.warmachine.model.LeagueStats;
import com.sbowling.warmachine.model.MetricCalibration;
import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.player.service.PlayerService;
import com.sbowling.warmachine.war.batting.BattingMetric;
import com.sbowling.warmachine.war.batting.BattingMetricRegistry;
import com.sbowling.warmachine.war.batting.BattingRunsCalculator;
import com.sbowling.warmachine.war.batting.impl.BattingAverage;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BattingServiceTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private LeagueService leagueService;

    @Mock
    private BattingMetricRegistry metricRegistry;

    @Mock
    private BattingRunsCalculator battingRunsCalculator;

    private BattingService battingService;

    @BeforeEach
    void setUp() {
        battingService = new BattingService(
                playerService,
                leagueService,
                metricRegistry,
                battingRunsCalculator
        );
    }

    @Test
    void calculatesBattingRuns() {
        int playerId = 592450;
        int season = 2025;
        String abbreviation = "AVG";

        PlayerStats playerStats = new PlayerStats(
                100,
                600,
                600,
                180,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );

        LeagueStats leagueStats = new LeagueStats(
                season,
                Map.of("AVG", 0.250)
        );

        MetricCalibration calibration =
                new MetricCalibration(season, abbreviation, 0.00125);

        BattingMetric metric = new BattingAverage();

        when(playerService.getPlayerStats(playerId, season))
                .thenReturn(playerStats);

        when(leagueService.getLeagueStats(season))
                .thenReturn(leagueStats);

        when(metricRegistry.getMetric(abbreviation))
                .thenReturn(metric);

        when(battingRunsCalculator.calculate(
                playerStats,
                leagueStats,
                metric,
                calibration
        )).thenReturn(24.0);

        double result = battingService.calculateBattingRuns(
                playerId,
                season,
                abbreviation,
                calibration
        );

        assertEquals(24.0, result);

        verify(playerService).getPlayerStats(playerId, season);
        verify(leagueService).getLeagueStats(season);
        verify(metricRegistry).getMetric(abbreviation);
        verify(battingRunsCalculator).calculate(
                playerStats,
                leagueStats,
                metric,
                calibration
        );
    }
}