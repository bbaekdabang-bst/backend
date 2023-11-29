package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.debates.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findPostDetailByPostId(Long postId);
}
