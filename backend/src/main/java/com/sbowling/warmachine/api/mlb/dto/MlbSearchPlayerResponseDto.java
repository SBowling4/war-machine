package com.sbowling.warmachine.api.mlb.dto;

import java.util.List;

public record MlbSearchPlayerResponseDto(List<Player> people) {

  public record Player(
      int id, String fullName, Team currentTeam, Position primaryPosition, boolean active) {}

  public record Team(String name) {}

  public record Position(String name, String abbreviation) {}
}
