package com.sbowling.warmachine.api.mlb.mapper;

import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.player.model.PlayerStats;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MlbStatsMapper {
  public PlayerStats toPlayerStats(MlbStatsResponseDto response) {
    return toPlayerStats(response.stats().getFirst().splits().getFirst());
  }

  public PlayerStats toPlayerStats(MlbStatsResponseDto.Split split) {
    MlbStatsResponseDto.Stat stat = split.stat();

    return new PlayerStats(
        stat.gamesPlayed(),
        stat.plateAppearances(),
        stat.atBats(),
        stat.hits(),
        stat.doubles(),
        stat.triples(),
        stat.homeRuns(),
        stat.baseOnBalls(),
        stat.intentionalWalks(),
        stat.hitByPitch(),
        stat.stolenBases(),
        stat.caughtStealing(),
        stat.groundIntoDoublePlay(),
        stat.sacBunts(),
        stat.sacFlies(),
        stat.runs(),
        stat.rbi());
  }

  public List<PlayerStats> toPlayerStatsList(MlbStatsResponseDto response) {
    return response.stats().getFirst().splits().stream().map(this::toPlayerStats).toList();
  }
}
