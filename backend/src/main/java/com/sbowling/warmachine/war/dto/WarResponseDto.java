package com.sbowling.warmachine.war.dto;

public record WarResponseDto(
        int playerId,
        int season,
        String battingMetric,
        double battingRuns,
        double baserunningRuns,
        double fieldingRuns,
        double replacementRuns,
        double runsAboveReplacement,
        double war
) {}