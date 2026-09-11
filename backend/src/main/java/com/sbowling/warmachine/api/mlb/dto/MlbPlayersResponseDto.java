package com.sbowling.warmachine.api.mlb.dto;

import java.util.List;

public record MlbPlayersResponseDto(
        List<Player> people
) {

    public record Player(
            int id,
            String fullName,
            boolean active,
            Position primaryPosition,
            Team currentTeam
    ) {}

    public record Position(
            String abbreviation
    ) {}

    public record Team(
            String name
    ) {}
}