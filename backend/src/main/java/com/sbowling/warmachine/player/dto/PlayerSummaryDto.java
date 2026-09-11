package com.sbowling.warmachine.player.dto;

public record PlayerSummaryDto(
        int id,
        String name,
        String position,
        String team,
        boolean active
) {}