package com.sbowling.warmachine.player.service;

import com.sbowling.warmachine.mlb.MlbApiClient;
import com.sbowling.warmachine.mlb.MlbStatsMapper;
import com.sbowling.warmachine.mlb.dto.MlbPlayerResponseDto;
import com.sbowling.warmachine.mlb.dto.MlbPlayersResponseDto;
import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.player.dto.PlayerSummaryDto;
import com.sbowling.warmachine.player.mapper.PlayerMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

  private final MlbApiClient mlbApiClient;
  private final MlbStatsMapper mlbStatsMapper;
  private final PlayerMapper playerMapper;

  public PlayerService(
          MlbApiClient mlbApiClient,
          MlbStatsMapper mlbStatsMapper,
          PlayerMapper playerMapper
  ) {
    this.mlbApiClient = mlbApiClient;
    this.mlbStatsMapper = mlbStatsMapper;
    this.playerMapper = playerMapper;
  }

  public PlayerStats getPlayerStats(int playerId, int season) {
    var response = mlbApiClient.getPlayerStats(playerId, season);

    return mlbStatsMapper.toPlayerStats(response);
  }

  public List<PlayerSummaryDto> getPlayers(int season) {
    MlbPlayersResponseDto response =
            mlbApiClient.getPlayers(season);

    return playerMapper.toPlayerSummaries(response);
  }

  public MlbPlayerResponseDto getPlayer(int playerId) {
    return mlbApiClient.getPlayer(playerId);
  }
}