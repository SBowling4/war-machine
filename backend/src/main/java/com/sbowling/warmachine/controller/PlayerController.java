package com.sbowling.warmachine.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.sbowling.warmachine.mlb.dto.MlbPlayerResponseDto;
import com.sbowling.warmachine.model.PlayerStats;
import com.sbowling.warmachine.player.dto.PlayerSummaryDto;
import com.sbowling.warmachine.player.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PlayerController {
  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/api/players/{playerId}")
  public MlbPlayerResponseDto getPlayer(@PathVariable int playerId) {
    return playerService.getPlayer(playerId);
  }

  @GetMapping("/api/players/{playerId}/stats")
  public PlayerStats getPlayerStats(@PathVariable int playerId, @RequestParam int season) {
    return playerService.getPlayerStats(playerId, season);
  }

  @GetMapping("api/players")
  public List<PlayerSummaryDto> getPlayers(@RequestParam int season) {
    return playerService.getPlayers(season);
  }
}
