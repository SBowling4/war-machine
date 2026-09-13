package com.sbowling.warmachine.war.calibration.service;

import com.sbowling.warmachine.api.mlb.MlbApiClient;
import com.sbowling.warmachine.api.mlb.mapper.MlbStatsMapper;
import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.calibration.LinearRegression;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.repository.MetricCalibrationRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MetricCalibrationService {
  private static final int TRAILING_SEASONS = 5;

  private final MetricCalibrationRepository repository;
  private final MlbApiClient mlbApiClient;
  private final MlbStatsMapper mlbStatsMapper;

  public MetricCalibrationService(
          MetricCalibrationRepository repository,
          MlbApiClient mlbApiClient,
          MlbStatsMapper mlbStatsMapper) {
    this.repository = repository;
    this.mlbApiClient = mlbApiClient;
    this.mlbStatsMapper = mlbStatsMapper;
  }

  public MetricCalibration getCalibration(int season, BattingMetric metric) {
    return repository
            .findBySeasonAndMetricAbbreviation(season, metric.getAbbreviation())
            .orElseGet(() -> calculateAndSave(season, metric));
  }

  private MetricCalibration calculateAndSave(int season, BattingMetric metric) {
    List<PlayerStats> teamSeasons = fetchTrailingTeamSeasons(season);
    double scale = calculateScale(teamSeasons, metric);

    MetricCalibration calibration = new MetricCalibration(season, metric.getAbbreviation(), scale);
    return repository.save(calibration);
  }

  private List<PlayerStats> fetchTrailingTeamSeasons(int season) {
    List<PlayerStats> pooled = new ArrayList<>();

    for (int year = season - TRAILING_SEASONS + 1; year <= season; year++) {
      MlbStatsResponseDto response = mlbApiClient.getTeamHittingStats(year);
      pooled.addAll(mlbStatsMapper.toPlayerStatsList(response));
    }

    return pooled;
  }

  private double calculateScale(List<PlayerStats> teamSeasons, BattingMetric metric) {
    double[] x = new double[teamSeasons.size()];
    double[] y = new double[teamSeasons.size()];

    int validTeamSeasons = 0;

    for (PlayerStats teamSeason : teamSeasons) {
      if (teamSeason.plateAppearances() == 0) continue;

      x[validTeamSeasons] = metric.calculate(teamSeason);
      y[validTeamSeasons] = (double) teamSeason.runs() / teamSeason.plateAppearances();
      validTeamSeasons++;
    }

    double[] validX = Arrays.copyOf(x, validTeamSeasons);
    double[] validY = Arrays.copyOf(y, validTeamSeasons);

    LinearRegression regression = LinearRegression.calculate(validX, validY);

    if (regression.slope() == 0.0) {
      throw new IllegalStateException("Calculated regression slope is zero.");
    }

    return 1.0 / regression.slope();
  }
}