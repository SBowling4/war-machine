package com.sbowling.warmachine.war.controller;

import com.sbowling.warmachine.war.dto.WarResponseDto;
import com.sbowling.warmachine.war.service.WarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/war")
@CrossOrigin(origins = "http://localhost:5173")
public class WarController {

    private final WarService warService;

    public WarController(WarService warService) {
        this.warService = warService;
    }

    @GetMapping
    public WarResponseDto calculateWar(
            @RequestParam int playerId,
            @RequestParam int season,
            @RequestParam String metric
    ) {
        return warService.calculateWar(
                playerId,
                season,
                metric
        );
    }

    @GetMapping("/compare")
    public List<WarResponseDto> compareMetrics(
            @RequestParam int playerId,
            @RequestParam int season) {
        return warService.compareMetrics(playerId, season);
    }
}