package com.bbacks.bst.debates.repository;

import com.bbacks.bst.debates.domain.Debate;
import com.bbacks.bst.debates.domain.UserDebate;
import com.bbacks.bst.debates.dto.MyDebateResponse;
import com.bbacks.bst.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDebateRepository extends JpaRepository<UserDebate, Long> {
    List<UserDebate> findByUser (User user);
}

