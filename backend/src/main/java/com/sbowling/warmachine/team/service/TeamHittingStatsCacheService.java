package com.sbowling.warmachine.team.service;

import com.sbowling.warmachine.api.mlb.MlbApiClient;
import com.sbowling.warmachine.api.mlb.dto.MlbStatsResponseDto;
import com.sbowling.warmachine.api.mlb.mapper.MlbStatsMapper;
import com.sbowling.warmachine.player.model.PlayerStats;
import com.sbowling.warmachine.team.model.TeamHittingSeason;
import com.sbowling.warmachine.team.repository.TeamHittingSeasonRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TeamHittingStatsCacheService {

    private final TeamHittingSeasonRepository repository;
    private final MlbApiClient mlbApiClient;
    private final MlbStatsMapper mlbStatsMapper;

    public TeamHittingStatsCacheService(
            TeamHittingSeasonRepository repository,
            MlbApiClient mlbApiClient,
            MlbStatsMapper mlbStatsMapper) {
        this.repository = repository;
        this.mlbApiClient = mlbApiClient;
        this.mlbStatsMapper = mlbStatsMapper;
    }

    public List<PlayerStats> getTeamSeasonStats(int season) {
        List<TeamHittingSeason> cached = repository.findBySeason(season);

        if (!cached.isEmpty()) {
            return cached.stream().map(TeamHittingSeason::toPlayerStats).toList();
        }

        return fetchAndCache(season);
    }

    private List<PlayerStats> fetchAndCache(int season) {
        MlbStatsResponseDto response = mlbApiClient.getTeamHittingStats(season);
        List<MlbStatsResponseDto.Split> splits = response.stats().getFirst().splits();

        List<TeamHittingSeason> entities =
                splits.stream()
                        .map(
                                split ->
                                        new TeamHittingSeason(
                                                season, split.team().id(), split.team().name(),
                                                mlbStatsMapper.toPlayerStats(split)))
                        .toList();

        repository.saveAll(entities);

        return entities.stream().map(TeamHittingSeason::toPlayerStats).toList();
    }
}