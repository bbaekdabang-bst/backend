package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.debates.domain.UserDebate;
import com.bbacks.bst.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDebateRepository extends JpaRepository<UserDebate, Long> {
    List<UserDebate> findByUser (User user);
}

