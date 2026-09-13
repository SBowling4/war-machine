package com.sbowling.warmachine.war;

import org.springframework.stereotype.Component;

@Component
public class PrototypeWarConfig {
    private final double runsPerWin = 10.0;
    private final double replacementRunsBelowAveragePer600Pa = 20.0;

    public double getReplacementRunsPerPlateAppearance() {
        return replacementRunsBelowAveragePer600Pa / 600.0;
    }

    public double getRunsPerWin() {
        return runsPerWin;
    }
}
