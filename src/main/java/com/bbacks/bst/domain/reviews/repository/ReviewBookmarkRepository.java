package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.domain.ReviewBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBookmarkRepository extends JpaRepository<ReviewBookmark, Long> {
    Optional<ReviewBookmark> findByUserUserIdAndReviewReviewId(Long userId, Long reviewId);
}
