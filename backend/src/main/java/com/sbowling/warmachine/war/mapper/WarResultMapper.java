package com.sbowling.warmachine.war.mapper;

import com.sbowling.warmachine.war.model.WarResult;
import com.sbowling.warmachine.war.dto.WarResponseDto;
import org.springframework.stereotype.Component;

@Component
public class WarResultMapper {

    public WarResponseDto toDto(
            WarResult result,
            int playerId,
            int season,
            String battingMetric
    ) {
        return new WarResponseDto(
                playerId,
                season,
                battingMetric,
                result.battingRuns(),
                result.baserunningRuns(),
                result.fieldingRuns(),
                result.replacementRuns(),
                result.runsAboveReplacement(),
                result.war()
        );
    }
}