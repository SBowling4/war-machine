package com.sbowling.warmachine.war.batting.metric.impl;

import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.war.batting.metric.BattingMetric;
import org.springframework.stereotype.Component;

@Component
public class IsolatedPower implements BattingMetric {

    @Override
    public String getName() {
        return "Isolated Power";
    }

    @Override
    public String getAbbreviation() {
        return "ISO";
    }

    @Override
    public double calculate(PlayerStats stats) {
        if (stats.atBats() == 0) return 0.0;

        double slugging =
                (double) (stats.singles() + 2 * stats.doubles() + 3 * stats.triples() + 4 * stats.homeRuns())
                        / stats.atBats();
        double battingAverage = (double) stats.hits() / stats.atBats();

        return slugging - battingAverage;
    }

    @Override
    public double getOpportunity(PlayerStats stats) {
        return stats.atBats();
    }
}