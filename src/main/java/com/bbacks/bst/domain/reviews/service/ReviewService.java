package com.bbacks.bst.domain.reviews.service;

import com.bbacks.bst.domain.books.domain.Book;
import com.bbacks.bst.domain.reviews.domain.Review;
import com.bbacks.bst.domain.reviews.domain.ReviewBookmark;
import com.bbacks.bst.domain.reviews.domain.ReviewComment;
import com.bbacks.bst.domain.reviews.dto.*;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.domain.books.repository.BookRepository;
import com.bbacks.bst.domain.reviews.repository.ReviewBookmarkRepository;
import com.bbacks.bst.domain.reviews.repository.ReviewCommentRepository;
import com.bbacks.bst.domain.reviews.repository.ReviewRepository;
import com.bbacks.bst.global.utils.S3Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.bbacks.bst.domain.reviews.domain.QReview.review;
import static com.bbacks.bst.domain.reviews.domain.QReviewComment.reviewComment;
import static com.bbacks.bst.domain.user.domain.QUser.user;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewBookmarkRepository reviewBookmarkRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewCommentRepository commentRepository;

    private final S3Service s3Service;
    private final JPAQueryFactory queryFactory;

//    // offset, limit 사용하여 페이징 처리
//    @Transactional(readOnly = true)
//    public Page<ReviewInBookDetailResponse> getBookDetailReview(Long bookId, Pageable pageable){
//        Book book = bookRepository.getReferenceById(bookId);
//        return reviewRepository.findAllByBook(book, pageable)
//                .map(ReviewInBookDetailResponse::from);
//    }

    @Transactional(readOnly = true)
    public List<ReviewInBookDetailResponse> getBookDetailReviewNoOffset(Long bookId, Long reviewId){
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


    @Transactional(readOnly = true)
    public ReviewDetailResponse getReviewDetail(Long reviewId){
        ReviewDetailResponse response =queryFactory
                .select(new QReviewDetailResponse(
                            review.reviewTitle,
                            review.reviewContent,
                            review.reviewImg,
                            user.userNickname,
                            user.userPhoto
                    ))
                .from(review)
                .innerJoin(review.user, user)
                .where(review.reviewId.eq(reviewId))
                .fetchOne();
        List<ReviewDetailCommentResponse> comments = queryFactory
                .select(new QReviewDetailCommentResponse(
                        user.userId,
                        user.userNickname,
                        user.userPhoto,
                        reviewComment.commentText
                ))
                .from(reviewComment)
                .innerJoin(reviewComment.user, user)
                .where(reviewComment.review.reviewId.eq(reviewId))
                .fetch();

        response.setReviewComments(comments);
        return response;

    }



    @Transactional
    public Long bookmarkReview(Long userId, Long reviewId){
        Optional<ReviewBookmark> checkBookmark = reviewBookmarkRepository.findByUserUserIdAndReviewReviewId(userId, reviewId);
        if(checkBookmark.isEmpty()){
            /**
             * 없으면 insert
             */
            User user = userRepository.getReferenceById(userId);
            Review review = reviewRepository.getReferenceById(reviewId);
            ReviewBookmark bookmark = new ReviewBookmark(user, review);
            return reviewBookmarkRepository.save(bookmark).getBookmarkId();
        } else {
            /**
             * 이미 있으면 delete
             */
            Long bookmarkId = checkBookmark.get().getBookmarkId();
            reviewBookmarkRepository.deleteById(bookmarkId);
            return 0L;
        }
    }

    @Transactional
    public Long postBookReview(Long bookId, Long userId, ReviewRequest reviewRequest, MultipartFile file) {
        User user = userRepository.getReferenceById(userId);
        Book book = bookRepository.getReferenceById(bookId);

        Review review = Review.builder()
                .user(user)
                .book(book)
                .reviewTitle(reviewRequest.getReviewTitle())
                .reviewContent(reviewRequest.getReviewContent())
                .reviewSpoiler(reviewRequest.getReviewSpoiler())
                .reviewPrivate(reviewRequest.getReviewPrivate())
                .build();

        if(file != null && !file.isEmpty()){
            fileUpload(review, file);
        }

        return reviewRepository.save(review).getReviewId();

    }

    @Transactional
    public Long postReviewComment(Long reviewId, Long userId, ReviewCommentRequest commentRequest) {
        User user = userRepository.getReferenceById(userId);
        Review review = reviewRepository.getReferenceById(reviewId);

        ReviewComment reviewComment = ReviewComment.builder()
                .review(review)
                .user(user)
                .commentText(commentRequest.getComment())
                .build();

        return commentRepository.save(reviewComment).getCommentId();
    }

    private void fileUpload(Review review, MultipartFile file) {
        String fileName = s3Service.uploadFile(file);
        review.setReviewImg(fileName);
    }



    /**
     * 좋아요 구현할 때 참고
     */
//    public void redisPractice(Long userId, Long reviewId){
//        final String redisUserId = "user"+userId.toString();
//        final String redisReviewId = "review"+reviewId.toString();
//
//        final long timeStamp = System.currentTimeMillis();
//        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
//
//        stringStringZSetOperations.add(redisUserId, redisReviewId, timeStamp);
//    }

}
