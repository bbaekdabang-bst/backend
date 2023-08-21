package com.bbacks.bst.domain.user.service;

import com.bbacks.bst.domain.user.dto.UserInfo;
import com.bbacks.bst.global.exception.UserIdNotFoundException;
import com.bbacks.bst.domain.reviews.domain.Review;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.dto.UserPageResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewListResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewResponse;
import com.bbacks.bst.domain.user.repository.UserRepository;
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

    /**
     * 유저 프로필 변경 (빈 문자열일 경우 update X)
     * @param userId : api 호출하는 사용자 id
     * @param userUpdateInfo : update할 프로필 객체
     */
    @Transactional
    public UserInfo updateProfile(Long userId, UserInfo userUpdateInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);

        String userNickname = userUpdateInfo.getUserNickname();
        String userPhoto = userUpdateInfo.getUserPhoto();

        if(!userPhoto.isEmpty()) {
            user.updatePhoto(userPhoto);
        }
        if(!userNickname.isEmpty()) {
            user.updateNickname(userNickname);
        }

        userRepository.save(user);

        return UserInfo.builder()
                .userNickname(userNickname)
                .userPhoto(userPhoto)
                .build();
    }

}