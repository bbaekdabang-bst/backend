package com.bbacks.bst.reviews.repository;

import com.bbacks.bst.reviews.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserUserIdAndReviewReviewId(Long userId, Long reviewId);
}
