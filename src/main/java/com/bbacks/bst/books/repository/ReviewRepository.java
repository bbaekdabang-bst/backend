package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<ReviewDetail> findByReviewId(Long reviewId);
}
