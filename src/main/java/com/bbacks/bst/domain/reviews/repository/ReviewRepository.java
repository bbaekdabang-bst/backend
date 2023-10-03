package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.domain.Review;
import com.bbacks.bst.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Review findByReviewId(Long reviewId);
    List<Review> findAllByUser(User user);
//    // offset, limit 을 통한 페이징 처리
//    Page<Review> findAllByBook(Book book, Pageable pageable);

}
