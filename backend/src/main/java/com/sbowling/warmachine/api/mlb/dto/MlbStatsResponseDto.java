package com.sbowling.warmachine.api.mlb.dto;

import java.util.List;

public record MlbStatsResponseDto(List<StatsGroup> stats) {
  public record StatsGroup(List<Split> splits) {}

  public record Split(
      String season, Stat stat, Team team, Player player, League league, String gameType) {}

  public record Stat(
      int gamesPlayed,
      int runs,
      int doubles,
      int triples,
      int homeRuns,
      int strikeOuts,
      int baseOnBalls,
      int intentionalWalks,
      int hits,
      int hitByPitch,
      double avg,
      int atBats,
      double obp,
      double slg,
      String ops,
      int caughtStealing,
      int stolenBases,
      int groundIntoDoublePlay,
      int plateAppearances,
      int totalBases,
      int rbi,
      int leftOnBase,
      int sacBunts,
      int sacFlies,
      String babip) {}

  public record Team(int id, String name) {}

  public record Player(int id, String fullName) {}

  public record League(int id, String name) {}
}
