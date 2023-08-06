package com.bbacks.bst.debates.repository;

import com.bbacks.bst.debates.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostQuotationId(Long postId);
}
