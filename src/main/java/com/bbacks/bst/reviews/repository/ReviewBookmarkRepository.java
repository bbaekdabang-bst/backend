package com.bbacks.bst.reviews.repository;

import com.bbacks.bst.reviews.domain.ReviewBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBookmarkRepository extends JpaRepository<ReviewBookmark, Long> {
    Optional<ReviewBookmark> findByUserUserIdAndReviewReviewId(Long userId, Long reviewId);
}
