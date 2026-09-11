package com.sbowling.warmachine.mlb;

import com.sbowling.warmachine.mlb.dto.MlbPlayerResponseDto;
import com.sbowling.warmachine.mlb.dto.MlbPlayersResponseDto;
import com.sbowling.warmachine.mlb.dto.MlbSearchPlayerResponseDto;
import com.sbowling.warmachine.mlb.dto.MlbStatsResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MlbApiClient {

  private static final String MLB_API_URL = "https://statsapi.mlb.com/api/v1";

  private final RestClient restClient;

  public MlbApiClient(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl(MLB_API_URL).build();
  }

  public MlbSearchPlayerResponseDto searchPlayers(String name) {
    return restClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/people/search").queryParam("names", name).build())
        .retrieve()
        .body(MlbSearchPlayerResponseDto.class);
  }

  public MlbPlayerResponseDto getPlayer(int playerId) {
    return restClient
        .get()
        .uri("/people/{playerId}", playerId)
        .retrieve()
        .body(MlbPlayerResponseDto.class);
  }

  public MlbStatsResponseDto getPlayerStats(int playerId, int season) {
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/people/{playerId}/stats")
                    .queryParam("stats", "season")
                    .queryParam("group", "hitting")
                    .queryParam("season", season)
                    .build(playerId))
        .retrieve()
        .body(MlbStatsResponseDto.class);
  }

  public MlbStatsResponseDto getLeagueHittingStats(int season) {
    final int limit = 1000;
    int offset = 0;

    List<MlbStatsResponseDto.Split> allSplits = new ArrayList<>();

    while (true) {
      int currentOffset = offset;

      MlbStatsResponseDto response =
          restClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/stats")
                          .queryParam("stats", "season")
                          .queryParam("group", "hitting")
                          .queryParam("season", season)
                          .queryParam("sportId", 1)
                          .queryParam("gameType", "R")
                          .queryParam("playerPool", "All")
                          .queryParam("limit", limit)
                          .queryParam("offset", currentOffset)
                          .build())
              .retrieve()
              .body(MlbStatsResponseDto.class);

      List<MlbStatsResponseDto.Split> splits = response.stats().getFirst().splits();

      allSplits.addAll(splits);

      if (splits.size() < limit) {
        break;
      }

      offset += limit;
    }

    return new MlbStatsResponseDto(List.of(new MlbStatsResponseDto.StatsGroup(allSplits)));
  }

  public MlbPlayersResponseDto getPlayers(int season) {
    return restClient
            .get()
            .uri(
                    uriBuilder ->
                            uriBuilder
                                    .path("/sports/1/players")
                                    .queryParam("season", season)
                                    .build()
            )
            .retrieve()
            .body(MlbPlayersResponseDto.class);
  }
}
