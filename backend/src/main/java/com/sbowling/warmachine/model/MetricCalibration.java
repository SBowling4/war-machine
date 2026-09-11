package com.sbowling.warmachine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "metric_calibrations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"season", "metric_abbreviation"}))
public class MetricCalibration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int season;
  private String metricAbbreviation;
  private double scale;

  protected MetricCalibration() {}

  public MetricCalibration(int season, String metricAbbreviation, double scale) {
    this.season = season;
    this.metricAbbreviation = metricAbbreviation;
    this.scale = scale;
  }

  public Long getId() {
    return id;
  }

  public int getSeason() {
    return season;
  }

  public String getMetricAbbreviation() {
    return metricAbbreviation;
  }

  public double getScale() {
    return scale;
  }
}
