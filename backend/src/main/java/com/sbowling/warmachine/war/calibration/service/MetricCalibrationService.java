package com.sbowling.warmachine.war.calibration.service;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.team.service.TeamHittingStatsCacheService;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import com.sbowling.warmachine.war.calibration.LinearRegression;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.war.repository.MetricCalibrationRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MetricCalibrationService {

  private static final int TRAILING_SEASONS = 5;
  private static final int MINIMUM_TEAM_SEASONS = 100; // ~2/3 of expected 150 (30 teams x 5 yrs)
  private static final int MINIMUM_TEAM_PLATE_APPEARANCES = 1000; // filters partial/garbage rows

  private final MetricCalibrationRepository repository;
  private final TeamHittingStatsCacheService teamHittingStatsCacheService;

  public MetricCalibrationService(
          MetricCalibrationRepository repository,
          TeamHittingStatsCacheService teamHittingStatsCacheService) {
    this.repository = repository;
    this.teamHittingStatsCacheService = teamHittingStatsCacheService;
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
      pooled.addAll(teamHittingStatsCacheService.getTeamSeasonStats(year));
    }

    return pooled;
  }

  private double calculateScale(List<PlayerStats> teamSeasons, BattingMetric metric) {
    if (teamSeasons.size() < MINIMUM_TEAM_SEASONS) {
      throw new IllegalStateException(
              "Only "
                      + teamSeasons.size()
                      + " team-seasons available; need at least "
                      + MINIMUM_TEAM_SEASONS
                      + " to calibrate reliably. Check that the MLB API returned data for all trailing seasons.");
    }

    List<PlayerStats> validTeamSeasons =
            teamSeasons.stream()
                    .filter(teamSeason -> teamSeason.plateAppearances() >= MINIMUM_TEAM_PLATE_APPEARANCES)
                    .toList();

    double[] x = new double[validTeamSeasons.size()];
    double[] y = new double[validTeamSeasons.size()];

    for (int i = 0; i < validTeamSeasons.size(); i++) {
      PlayerStats teamSeason = validTeamSeasons.get(i);
      x[i] = metric.calculate(teamSeason);
      y[i] = (double) teamSeason.runs() / teamSeason.plateAppearances();
    }

    LinearRegression regression = LinearRegression.calculate(x, y);
    double slope = regression.slope();

    if (slope == 0.0 || Double.isNaN(slope) || Double.isInfinite(slope)) {
      throw new IllegalStateException(
              "Calculated an invalid regression slope ("
                      + slope
                      + ") for metric "
                      + metric.getAbbreviation()
                      + "; this points to a data problem upstream, not a weak metric.");
    }

    return 1.0 / slope;
  }
}