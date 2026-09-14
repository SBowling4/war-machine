package com.sbowling.warmachine.team.repository;

import com.sbowling.warmachine.team.model.TeamHittingSeason;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamHittingSeasonRepository extends JpaRepository<TeamHittingSeason, Long> {
    List<TeamHittingSeason> findBySeason(int season);
}