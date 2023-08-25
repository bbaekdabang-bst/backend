package com.bbacks.bst.domain.user.controller;


import com.bbacks.bst.domain.user.dto.UserInfo;
import com.bbacks.bst.domain.user.dto.UserPageResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewListResponse;
import com.bbacks.bst.domain.user.dto.UserPageReviewResponse;
import com.bbacks.bst.domain.user.service.UserService;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
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

    // 프로필 변경
    @PostMapping("/update")
    public ApiResponseDto<?> updateUserInfo(Authentication authentication, @RequestParam("userNickname") String userNickname, @RequestParam("userPhoto") MultipartFile file) {
        Long userId = getUserId(authentication);
        UserInfo updateResponse = userService.updateProfile(userId, userNickname, file);
        return ApiResponseDto.success(SuccessStatus.UPDATE_POST_SUCCESS, updateResponse);
    }

}
