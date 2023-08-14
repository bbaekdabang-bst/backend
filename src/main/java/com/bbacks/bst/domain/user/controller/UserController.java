package com.bbacks.bst.domain.user.controller;


/**
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


}
**/
