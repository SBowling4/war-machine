package com.sbowling.warmachine.war.repository;

import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricCalibrationRepository extends JpaRepository<MetricCalibration, Long> {
  Optional<MetricCalibration> findBySeasonAndMetricAbbreviation(
      int season, String metricAbbreviation);
}
