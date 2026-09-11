package com.sbowling.warmachine.league.service;

import com.sbowling.warmachine.api.mlb.MlbApiClient;
import com.sbowling.warmachine.api.mlb.MlbLeagueStatsMapper;
import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.league.model.LeagueStats;
import org.springframework.stereotype.Service;

@Service
public class LeagueService {
  private final MlbApiClient mlbApiClient;
  private final MlbLeagueStatsMapper mlbLeagueStatsMapper;

  public LeagueService(MlbApiClient mlbApiClient, MlbLeagueStatsMapper mlbLeagueStatsMapper) {
    this.mlbApiClient = mlbApiClient;
    this.mlbLeagueStatsMapper = mlbLeagueStatsMapper;
  }

  public LeagueStats getLeagueStats(int season) {
    MlbStatsResponseDto response = mlbApiClient.getLeagueHittingStats(season);
    return mlbLeagueStatsMapper.toLeagueStats(response);
  }
}
