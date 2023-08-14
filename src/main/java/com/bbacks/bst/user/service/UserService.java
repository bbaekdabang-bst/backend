package com.bbacks.bst.user.service;

import com.bbacks.bst.common.exception.UserIdNotFoundException;
import com.bbacks.bst.reviews.domain.Review;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.dto.UserPageResponse;
import com.bbacks.bst.user.dto.UserPageReviewListResponse;
import com.bbacks.bst.user.dto.UserPageReviewResponse;
import com.bbacks.bst.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserPageResponse getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);
        return UserPageResponse.of(user);
    }

    @Transactional(readOnly = true)
    public List<UserPageReviewResponse> getMyPageReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);
        List<Review> reviews = user.getReviews();

        return reviews.stream()
                .limit(3)
                .map(this::mapReviewToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserPageReviewListResponse> getMyPageReviewList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);
        List<Review> reviews = user.getReviews();

        return reviews.stream()
                .map(this::mapReviewListToResponse)
                .collect(Collectors.toList());
    }

    private UserPageReviewListResponse mapReviewListToResponse(Review review) {
        return UserPageReviewListResponse.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .bookTitle(review.getBook().getBookTitle())
                .bookId(review.getBook().getBookId())
                .build();
    }

    private UserPageReviewResponse mapReviewToResponse(Review review) {
        return UserPageReviewResponse.builder()
                .reviewId(review.getReviewId())
                .bookImg(review.getBook().getBookImg())
                .bookId(review.getBook().getBookId())
                .build();
    }

}