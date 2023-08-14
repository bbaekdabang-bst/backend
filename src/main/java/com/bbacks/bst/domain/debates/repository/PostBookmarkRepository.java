package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.debates.domain.Post;
import com.bbacks.bst.domain.debates.domain.PostBookmark;
import com.bbacks.bst.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    boolean existsByUserAndPost(User user, Post post);
    @Transactional
    void deleteByUserAndPost(User user, Post post);

}
