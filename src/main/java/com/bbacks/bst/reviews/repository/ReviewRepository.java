package com.bbacks.bst.reviews.repository;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.reviews.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<ReviewDetail> findByReviewId(Long reviewId);

    Page<Review> findAllByBook(Book book, Pageable pageable);
}
