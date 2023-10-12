package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.domain.Review;
import com.bbacks.bst.domain.reviews.domain.ReviewBookmark;
import com.bbacks.bst.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewBookmarkRepository extends JpaRepository<ReviewBookmark, Long> {
    Optional<ReviewBookmark> findByUserUserIdAndReviewReviewId(Long userId, Long reviewId);

    Optional<ReviewBookmark> findByUserAndReview(User user, Review review);
}
