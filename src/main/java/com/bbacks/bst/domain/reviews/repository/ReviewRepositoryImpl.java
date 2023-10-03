package com.bbacks.bst.domain.reviews.repository;

import com.bbacks.bst.domain.reviews.dto.ReviewDetailCommentResponse;
import com.bbacks.bst.domain.reviews.dto.ReviewDetailResponse;
import com.bbacks.bst.domain.reviews.dto.ReviewInBookDetailResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bbacks.bst.domain.reviews.domain.QReview.review;
import static com.bbacks.bst.domain.reviews.domain.QReviewComment.reviewComment;
import static com.bbacks.bst.domain.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewInBookDetailResponse> findReviewByBookIdNoOffset(Long bookId, Long reviewId) {

        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (reviewId != null) {
            dynamicLtId.and(review.reviewId.lt(reviewId));
        }

        return queryFactory.select(
                        Projections.constructor(ReviewInBookDetailResponse.class,
                                review.reviewTitle,
                                review.reviewContent,
                                review.reviewId,
                                review.user.userNickname.as("reviewerNickname"),
                                review.user.userPhoto.as("reviewerImg")
                        ))
                .from(review)
                .innerJoin(review.user, user)
                .where(dynamicLtId
                        .and(review.book.bookId.eq(bookId)))
                .orderBy(review.reviewId.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public ReviewDetailResponse findReviewById(Long reviewId) {
        return queryFactory
                .select(
                        Projections.constructor(ReviewDetailResponse.class,
                                review.reviewTitle,
                                review.reviewContent,
                                review.reviewImg,
                                user.userNickname,
                                user.userPhoto))
                .from(review)
                .innerJoin(review.user, user)
                .where(review.reviewId.eq(reviewId))
                .fetchOne();
    }

    @Override
    public List<ReviewDetailCommentResponse> findReviewCommentById(Long reviewId) {
        return queryFactory
                .select(
                        Projections.constructor(ReviewDetailCommentResponse.class,
                                user.userId,
                                user.userNickname,
                                user.userPhoto,
                                reviewComment.commentText
                        ))
                .from(reviewComment)
                .innerJoin(reviewComment.user, user)
                .where(reviewComment.review.reviewId.eq(reviewId))
                .fetch();
    }


}
