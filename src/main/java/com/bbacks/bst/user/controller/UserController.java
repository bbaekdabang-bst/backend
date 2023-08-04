package com.bbacks.bst.user.controller;

import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import com.bbacks.bst.user.dto.UserPageResponse;
import com.bbacks.bst.user.dto.UserPageReviewListResponse;
import com.bbacks.bst.user.dto.UserPageReviewResponse;
import com.bbacks.bst.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class UserController {

    private final UserService userService;

    private Long getUserId(Authentication authentication){
        return Long.parseLong(authentication.getName());
    }

    @GetMapping
    public ApiResponseDto<?> getMyPage(Authentication authentication){

        Long userId = getUserId(authentication);
        UserPageResponse userPageResponse = userService.getMyPage(userId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, userPageResponse);
    }

    @GetMapping("/review")
    public ApiResponseDto<?> getMyPageReview(Authentication authentication){
        Long userId = getUserId(authentication);
        List<UserPageReviewResponse> reviewResponses = userService.getMyPageReview(userId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, reviewResponses);
    }

    @GetMapping("/review/list")
    public ApiResponseDto<?> getMyPageReviewList(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<UserPageReviewListResponse> reviewResponses = userService.getMyPageReviewList(userId);
        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, reviewResponses);
    }

}
