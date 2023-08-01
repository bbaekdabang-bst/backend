package com.bbacks.bst.books.service;

import com.bbacks.bst.books.domain.Bookmark;
import com.bbacks.bst.books.domain.Review;
import com.bbacks.bst.books.repository.BookmarkRepository;
import com.bbacks.bst.books.repository.ReviewDetail;
import com.bbacks.bst.books.repository.ReviewRepository;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ReviewDetail> getReviewDetail(Long reviewId){
        return reviewRepository.findByReviewId(reviewId);
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
