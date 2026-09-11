package com.sbowling.warmachine.player.mapper;

import com.sbowling.warmachine.api.mlb.dto.MlbPlayersResponseDto;
import com.sbowling.warmachine.player.dto.PlayerSummaryDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public List<PlayerSummaryDto> toPlayerSummaries(
            MlbPlayersResponseDto response
    ) {
        return response.people().stream()
                .map(player -> new PlayerSummaryDto(
                        player.id(),
                        player.fullName(),
                        player.primaryPosition().abbreviation(),
                        player.currentTeam().name(),
                        player.active()
                ))
                .toList();
    }
}