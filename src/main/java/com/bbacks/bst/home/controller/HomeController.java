package com.bbacks.bst.home.controller;

import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import com.bbacks.bst.home.dto.HomeResponse;
import com.bbacks.bst.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    // 메인 화면
    @GetMapping("/main")
    public ApiResponseDto<?> home() {
        HomeResponse homeResponse = homeService.home();

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, homeResponse);
    }

}