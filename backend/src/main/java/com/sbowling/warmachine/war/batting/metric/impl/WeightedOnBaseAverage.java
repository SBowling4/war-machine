package com.sbowling.warmachine.war.batting.metric.impl;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class WeightedOnBaseAverage implements BattingMetric {

    // Static linear weights from long-run published sabermetric research —
    // NOT derived from this project's own API data. See note below.
    private static final double UNINTENTIONAL_WALK_WEIGHT = 0.690;
    private static final double HIT_BY_PITCH_WEIGHT = 0.722;
    private static final double SINGLE_WEIGHT = 0.888;
    private static final double DOUBLE_WEIGHT = 1.271;
    private static final double TRIPLE_WEIGHT = 1.616;
    private static final double HOME_RUN_WEIGHT = 2.101;

    @Override
    public String getName() {
        return "Weighted On-Base Average";
    }

    @Override
    public String getAbbreviation() {
        return "wOBA";
    }

    @Override
    public double calculate(PlayerStats stats) {
        int unintentionalWalks = stats.walks() - stats.intentionalWalks();

        double numerator =
                UNINTENTIONAL_WALK_WEIGHT * unintentionalWalks
                        + HIT_BY_PITCH_WEIGHT * stats.hitByPitch()
                        + SINGLE_WEIGHT * stats.singles()
                        + DOUBLE_WEIGHT * stats.doubles()
                        + TRIPLE_WEIGHT * stats.triples()
                        + HOME_RUN_WEIGHT * stats.homeRuns();

        int denominator = stats.atBats() + unintentionalWalks + stats.sacrificeFlies() + stats.hitByPitch();

        return denominator == 0 ? 0.0 : numerator / denominator;
    }

    @Override
    public double getOpportunity(PlayerStats stats) {
        return stats.atBats() + (stats.walks() - stats.intentionalWalks()) + stats.sacrificeFlies() + stats.hitByPitch();
    }
}