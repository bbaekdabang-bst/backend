package com.bbacks.bst.domain.user.service;

import com.bbacks.bst.domain.user.dto.UserInfo;
import com.bbacks.bst.global.exception.NicknameOutOfRangeException;
import com.bbacks.bst.global.exception.UserIdNotFoundException;
import com.bbacks.bst.domain.reviews.domain.Review;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.dto.UserPageResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewListResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewResponse;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.global.utils.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

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
     * @param userNickname : 새 닉네임
     * @param file : 프로필 사진 파일
     */
    @Transactional
    public UserInfo updateProfile(Long userId, String userNickname, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);

        if(userNickname.length() > 20) { //닉네임 글자 수 제한
            throw new NicknameOutOfRangeException();
        }

        String imageLink = user.getUserPhoto(); //현재 프로필 사진 url

        if(!file.isEmpty()) {
            if(imageLink == null){ //기존 사진이 없었을 경우
                imageLink = s3Service.uploadFile(file); //새 사진 s3에 업로드
                user.updatePhoto(imageLink); //user 객체에 새 s3 링크 저장
                log.info("새 프로필 사진 업로드 완료!");
            }
            else {
                s3Service.deleteS3Object(imageLink); //기존 사진 s3에서 삭제
                imageLink = s3Service.uploadFile(file); //새 사진 s3에 업로드
                user.updatePhoto(imageLink); //user 객체에 새 s3 링크 저장
                log.info("기존 프로필 사진 삭제 후 새 프로필 사진 업로드 완료!");
            }

        }
        if(!userNickname.isEmpty()) {
            user.updateNickname(userNickname);
            log.info("닉네임 수정 완료!");
        }

        userRepository.save(user);

        return UserInfo.builder()
                .userNickname(user.getUserNickname())
                .userPhoto(imageLink)
                .build();
    }

}