package com.bbacks.bst.debates.repository;

import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebateRepository extends JpaRepository<Debate, Long> {
    @Modifying
    @Query(value = "UPDATE Debate d SET d.debateParticipants = d.debateParticipants + 1 WHERE d.debateId = :debateId")
    int incrementCountByDebateId(@Param("debateId") Long debateId);

    List<Debate> findTop10ByOrderByDebateParticipants();
    List<Debate> findTop10ByOrderByDebateCreatedAtDesc();

}
