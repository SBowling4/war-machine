package com.sbowling.warmachine.service;

import com.sbowling.warmachine.mlb.MlbApiClient;
import com.sbowling.warmachine.mlb.MlbLeagueStatsMapper;
import com.sbowling.warmachine.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.model.LeagueStats;
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
