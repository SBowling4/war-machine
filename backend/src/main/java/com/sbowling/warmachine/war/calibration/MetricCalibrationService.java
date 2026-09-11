package com.sbowling.warmachine.war.calibration;

import com.sbowling.warmachine.mlb.MlbApiClient;
import com.sbowling.warmachine.mlb.MlbStatsMapper;
import com.sbowling.warmachine.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.model.MetricCalibration;
import com.sbowling.warmachine.war.batting.BattingMetric;
import com.sbowling.warmachine.war.repository.MetricCalibrationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MetricCalibrationService {
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
    MlbStatsResponseDto response = mlbApiClient.getLeagueHittingStats(season);
    double scale = calculateScale(response, metric);

    MetricCalibration calibration = new MetricCalibration(season, metric.getAbbreviation(), scale);

    return repository.save(calibration);
  }

  private double calculateScale(MlbStatsResponseDto response, BattingMetric metric) {
    List<MlbStatsResponseDto.Split> splits = response.stats().getFirst().splits();

    double[] x = new double[splits.size()];
    double[] y = new double[splits.size()];

    int validPlayers = 0;

    for (MlbStatsResponseDto.Split split : splits) {
      MlbStatsResponseDto.Stat stat = split.stat();

      if (stat.plateAppearances() == 0) {
        continue;
      }

      x[validPlayers] = metric.calculate(mlbStatsMapper.toPlayerStats(split));

      y[validPlayers] = (double) stat.runs() / stat.plateAppearances();

      validPlayers++;
    }

    double[] validX = new double[validPlayers];
    double[] validY = new double[validPlayers];

    System.arraycopy(x, 0, validX, 0, validPlayers);
    System.arraycopy(y, 0, validY, 0, validPlayers);

    LinearRegression regression = LinearRegression.calculate(validX, validY);

    if (regression.slope() == 0.0) {
      throw new IllegalStateException("Calculated regression slope is zero.");
    }

    return 1.0 / regression.slope();
  }
}
