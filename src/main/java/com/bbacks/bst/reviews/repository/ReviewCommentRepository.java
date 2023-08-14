package com.bbacks.bst.reviews.repository;

import com.bbacks.bst.reviews.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

}
