package com.bbacks.bst.debates.repository;

import com.bbacks.bst.debates.domain.Post;
import com.bbacks.bst.debates.domain.PostBookmark;
import com.bbacks.bst.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    boolean existsByUserAndPost(User user, Post post);
    @Transactional
    void deleteByUserAndPost(User user, Post post);

}
