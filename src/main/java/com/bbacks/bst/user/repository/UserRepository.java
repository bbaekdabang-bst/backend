package com.bbacks.bst.user.repository;

import com.bbacks.bst.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
