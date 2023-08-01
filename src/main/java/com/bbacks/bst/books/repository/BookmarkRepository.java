package com.bbacks.bst.books.repository;

import com.bbacks.bst.books.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserUserIdAndReviewReviewId(Long userId, Long reviewId);
}
