package com.bbacks.bst.domain.debates.repository;

import com.bbacks.bst.domain.debates.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.bbacks.bst.domain.books.domain.QBook.book;
import static com.bbacks.bst.domain.debates.domain.QDebate.debate;
import static com.bbacks.bst.domain.debates.domain.QPost.post;
import static com.bbacks.bst.domain.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findPostDetailByPostId(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .where(post.postId.eq(postId))
                .leftJoin(post.debate, debate).fetchJoin()
                .leftJoin(debate.book, book).fetchJoin()
                .leftJoin(post.user, user).fetchJoin()
                .fetchOne());
    }
}
