package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.debates.domain.Debate;
import com.bbacks.bst.domain.debates.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findByPostQuotationId(Long postId);
    List<Post> findByDebate(Debate debate);

    List<Post> findByDebateOrderByPostCreatedAtDesc(Debate debate);
}
