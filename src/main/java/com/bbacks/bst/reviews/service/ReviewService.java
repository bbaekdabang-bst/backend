package com.bbacks.bst.reviews.service;

import com.bbacks.bst.books.domain.Book;
import com.bbacks.bst.books.domain.Bookmark;
import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.reviews.dto.ReviewDetailResponse;
import com.bbacks.bst.reviews.dto.ReviewInBookDetailResponse;
import com.bbacks.bst.reviews.dto.ReviewRequest;
import com.bbacks.bst.books.repository.BookRepository;
import com.bbacks.bst.books.repository.BookmarkRepository;
import com.bbacks.bst.reviews.repository.ReviewRepository;
import com.bbacks.bst.common.utils.S3Service;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.bbacks.bst.reviews.domain.QReview.review;
import static com.bbacks.bst.user.domain.QUser.user;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final S3Service s3Service;
    private final JPAQueryFactory queryFactory;

//    // offset, limit 사용하여 페이징 처리
//    @Transactional(readOnly = true)
//    public Page<BookDetailReviewResponse> getBookDetailReview(Long bookId, Pageable pageable){
//        Book book = bookRepository.getReferenceById(bookId);
//        return reviewRepository.findAllByBook(book, pageable)
//                .map(BookDetailReviewResponse::from);
//
//    }

    @Transactional(readOnly = true)
    public List<ReviewInBookDetailResponse> getBookDetailReviewNoOffset(Long bookId, Long reviewId){
        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (reviewId != null) {
            dynamicLtId.and(review.reviewId.lt(reviewId));
        }

        return queryFactory.select(Projections.constructor(ReviewInBookDetailResponse.class,
                        review.reviewTitle, review.reviewContent, review.reviewId, review.user.userNickname.as("reviewerNickname")))
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

        Review review = reviewRepository.findByReviewId(reviewId);
        return ReviewDetailResponse.from(review);
    }

    @Transactional
    public Long bookmarkReview(Long userId, Long reviewId){
        Optional<Bookmark> checkBookmark = bookmarkRepository.findByUserUserIdAndReviewReviewId(userId, reviewId);
        if(checkBookmark.isEmpty()){
            /**
             * 없으면 insert
             */
            User user = userRepository.getReferenceById(userId);
            Review review = reviewRepository.getReferenceById(reviewId);
            Bookmark bookmark = new Bookmark(user, review);
            return bookmarkRepository.save(bookmark).getBookmarkId();
        } else {
            /**
             * 이미 있으면 delete
             */
            Long bookmarkId = checkBookmark.get().getBookmarkId();
            bookmarkRepository.deleteById(bookmarkId);
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

        fileUpload(review, file);

        return reviewRepository.save(review).getReviewId();


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
