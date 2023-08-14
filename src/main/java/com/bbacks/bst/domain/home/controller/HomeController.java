package com.bbacks.bst.domain.home.controller;

import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.bbacks.bst.domain.debates.dto.MyDebateResponse;
import com.bbacks.bst.domain.home.dto.HomeResponse;
import com.bbacks.bst.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // 토론방 검색
    @GetMapping("/main/search")
    public ApiResponseDto<?> searchDebates(@RequestParam("keyword") String keyword) {
        List<MyDebateResponse> results = homeService.searchDebates(keyword);

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, results);
    }

    // 인기 토론방
    @GetMapping("/main/best")
    public ApiResponseDto<?> bestDebates() {
        List<MyDebateResponse> bestDebates = homeService.bestDebates();

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, bestDebates);
    }

    // 최신 토론방
    @GetMapping("/main/new")
    public ApiResponseDto<?> newDebates() {
        List<MyDebateResponse> newDebates = homeService.newDebates();

        return ApiResponseDto.success(SuccessStatus.GET_SUCCESS, newDebates);
    }

}