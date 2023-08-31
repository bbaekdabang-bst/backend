package com.bbacks.bst.domain.auth.controller;


import com.bbacks.bst.domain.auth.dto.AccessTokenResponse;
import com.bbacks.bst.domain.auth.dto.RefreshRequest;
import com.bbacks.bst.domain.auth.dto.TokenResponse;
import com.bbacks.bst.domain.auth.service.AuthService;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/login")
//    public ApiResponseDto<?> login() {
//        /**
//         * 회원가입하려고 가져온 사용자 정보를 토대로 토큰 발급
//         * DB에 없는 사람 가지고 토큰 만들면 안 됨
//         */
//        Long userId = 1L;
//
//        TokenResponse tokenResponse = authService.login(userId);
//        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenResponse);
//        /**
//         * refresh token도 클라이언트에 저장. httpOnly를 사용해서
//         */
//    }

    @PostMapping("/refresh")
    public ApiResponseDto<?> refreshToken(@RequestBody @Valid RefreshRequest request){
        String newAccessToken = authService.refresh(request.getRefreshKey());
        AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
        return ApiResponseDto.success(SuccessStatus.REFRESH_SUCCESS, accessTokenResponse);
    }

    @PostMapping("/logout")
    public ApiResponseDto<?> logout(Authentication authentication){
        Long userId = getUserId(authentication);
        authService.logout(userId);
        log.info("로그아웃됨");
        return ApiResponseDto.success(SuccessStatus.LOGOUT_SUCCESS);
    }

    private Long getUserId(Authentication authentication){
        return Long.parseLong(authentication.getName());
    }
}
