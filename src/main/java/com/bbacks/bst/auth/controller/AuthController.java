package com.bbacks.bst.auth.controller;


import com.bbacks.bst.auth.dto.TokenResponse;
import com.bbacks.bst.auth.service.AuthService;
import com.bbacks.bst.common.response.ApiResponseDto;
import com.bbacks.bst.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponseDto<?> login(){
        /**
         * 회원가입하려고 가져온 사용자 정보를 토대로 토큰 발급
         * DB에 없는 사람 가지고 토큰 만들면 안 됨
         */
        Long userId = 1L;

        TokenResponse tokenResponse = authService.login(userId);
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenResponse);
        /**
         * refresh token도 클라이언트에 저장. httpOnly를 사용해서
         */
    }
}
