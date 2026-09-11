package com.sbowling.warmachine.model;

import java.util.Map;

public record LeagueStats(int season, Map<String, Double> metricValues) {
  public double getValue(String abbreviation) {
    return metricValues.get(abbreviation);
  }
}
