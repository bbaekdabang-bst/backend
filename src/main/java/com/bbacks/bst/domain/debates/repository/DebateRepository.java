package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.debates.domain.Debate;
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

    List<Debate> findTop10ByOrderByDebateParticipantsDesc();
    List<Debate> findTop10ByOrderByDebateCreatedAtDesc();
    List<Debate> findByBook(Book book);
}
