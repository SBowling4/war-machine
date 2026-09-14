package com.sbowling.warmachine.war.calibration.controller;

import com.sbowling.warmachine.war.batting.metric.BattingMetricRegistry;
import com.sbowling.warmachine.war.calibration.model.MetricCalibration;
import com.sbowling.warmachine.war.calibration.service.MetricCalibrationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MetricCalibrationController {

    private final MetricCalibrationService calibrationService;
    private final BattingMetricRegistry metricRegistry;

    public MetricCalibrationController(
            MetricCalibrationService calibrationService, BattingMetricRegistry metricRegistry) {
        this.calibrationService = calibrationService;
        this.metricRegistry = metricRegistry;
    }

    @GetMapping("/api/calibration")
    public MetricCalibration getCalibration(
            @RequestParam int season, @RequestParam String metric) {
        return calibrationService.getCalibration(season, metricRegistry.getMetric(metric));
    }
}