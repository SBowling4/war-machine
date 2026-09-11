package com.sbowling.warmachine.war;

import org.springframework.stereotype.Component;

@Component
public class PrototypeWarConfig {
    private final double runsPerWin = 10.0;
    private final double replacementRunsPerGame = .294;

    public double getReplacementRunsPerGame() {
        return replacementRunsPerGame;
    }

    public double getRunsPerWin() {
        return runsPerWin;
    }
}
