package com.bbacks.bst.user.repository;

import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
}
