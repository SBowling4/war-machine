package com.sbowling.warmachine.war.batting.metric;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BattingMetricRegistry {
  private final List<BattingMetric> metrics;

  public BattingMetricRegistry(List<BattingMetric> metrics) {
    this.metrics = metrics;
  }

  public BattingMetric getMetric(String abbreviation) {
    return metrics.stream()
        .filter(metric -> metric.getAbbreviation().equals(abbreviation))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Metric not found: " + abbreviation));
  }
}
