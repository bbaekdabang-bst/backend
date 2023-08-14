package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

}
