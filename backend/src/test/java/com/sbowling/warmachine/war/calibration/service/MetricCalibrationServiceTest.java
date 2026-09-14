package com.sbowling.warmachine.war.calibration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.sbowling.warmachine.api.mlb.MlbApiClient;
import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.api.mlb.mapper.MlbStatsMapper;
import com.sbowling.warmachine.team.repository.TeamHittingSeasonRepository;
import com.sbowling.warmachine.team.service.TeamHittingStatsCacheService;
import com.sbowling.warmachine.war.batting.metric.impl.BattingAverage;
import com.sbowling.warmachine.war.batting.metric.impl.OnBasePercentage;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.war.repository.MetricCalibrationRepository;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class MetricCalibrationServiceTest {

    @Mock private MetricCalibrationRepository calibrationRepository;
    @Mock private TeamHittingSeasonRepository teamHittingSeasonRepository;
    @Mock private MlbApiClient mlbApiClient;

    private MetricCalibrationService calibrationService;

    @BeforeEach
    void setUp() throws Exception {
        MlbStatsMapper mlbStatsMapper = new MlbStatsMapper();
        TeamHittingStatsCacheService cacheService =
                new TeamHittingStatsCacheService(teamHittingSeasonRepository, mlbApiClient, mlbStatsMapper);
        calibrationService = new MetricCalibrationService(calibrationRepository, cacheService);

        MlbStatsResponseDto realSeason = loadFixture();

        when(teamHittingSeasonRepository.findBySeason(anyInt())).thenReturn(List.of());
        // Pooling the SAME real season 5x is mathematically identical to pooling it once —
        // repeating identical rows doesn't move a least-squares slope, only the sample size.
        // This exercises the real 5-year pooling path while still checking against a
        // known, independently-verified single-season answer.
        when(mlbApiClient.getTeamHittingStats(anyInt())).thenReturn(realSeason);
        when(calibrationRepository.save(any(MetricCalibration.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private MlbStatsResponseDto loadFixture() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/fixtures/team-hitting-2024.json")) {
            return new ObjectMapper().readValue(in, MlbStatsResponseDto.class);
        }
    }

    @Test
    void calibratesObpAgainstReal2024Data() {
        when(calibrationRepository.findBySeasonAndMetricAbbreviation(2024, "OBP"))
                .thenReturn(Optional.empty());

        MetricCalibration calibration = calibrationService.getCalibration(2024, new OnBasePercentage());

        // Verified independently against real 2024 MLB team hitting data: slope ≈ 0.7884
        assertEquals(1.27, calibration.getScale(), 0.01);
    }

    @Test
    void calibratesAvgAgainstReal2024Data() {
        when(calibrationRepository.findBySeasonAndMetricAbbreviation(2024, "AVG"))
                .thenReturn(Optional.empty());

        MetricCalibration calibration = calibrationService.getCalibration(2024, new BattingAverage());

        // AVG is expected to be a noisier predictor than OBP — that's the point of the
        // project, not a defect. This just locks in the known number so regressions get caught.
        assertEquals(1.38, calibration.getScale(), 0.02);
    }
}