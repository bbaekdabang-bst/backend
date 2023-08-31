package com.bbacks.bst.global.oauth2.handler;

import com.bbacks.bst.domain.auth.dto.TokenResponse;
import com.bbacks.bst.domain.auth.service.AuthService;
import com.bbacks.bst.global.jwt.service.JwtService;
import com.bbacks.bst.global.oauth2.CustomOAuth2User;
import com.bbacks.bst.global.oauth2.OAuthAttributes;
import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.bbacks.bst.global.response.SuccessStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final AuthService authService;
    private ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userSocialId = getSocialId(oAuth2User);

        String accessToken = jwtService.createAccessToken(userSocialId);
        String refreshToken = jwtService.createRefreshToken(userSocialId);

        log.info("AccessToken: " + accessToken);
        log.info("RefreshToken: " + refreshToken);
        log.info("Token 생성 완료!");

        String refreshKey = authService.saveRefreshTokenInRedis(refreshToken); //Redis에 저장
        authService.saveRefreshKey(refreshKey, userSocialId); //DB에 저장
        log.info("RefreshKey: " + refreshKey);
        log.info("Token 전송 및 저장 완료!");


        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//            response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        sendResponse(response, accessToken, refreshKey);

        //token 전송 & DB에 refreshToken 저장
//            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//            jwtService.updateRefreshToken(userSocialId, userPlatform.toString(), refreshToken);
//            response.sendRedirect("/api/v1/books");

    }

    private String getSocialId(CustomOAuth2User oAuth2User){
        PlatformType userPlatform = oAuth2User.getPlatformType(); //소셜 플랫폼(NAVER, KAKAO)
        String userAttributeName = oAuth2User.getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes(); //소셜 로그인 API가 제공하는 userInfo의 Json 값(유저 정보)
        OAuthAttributes extractAttributes = OAuthAttributes.of(userPlatform, userAttributeName, attributes);

        String userSocialId = extractAttributes.getOauth2UserInfo().getId();
        return userSocialId;
    }

    private void sendResponse(HttpServletResponse response, String accessToken, String refreshKey) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshKey(refreshKey)
                .build();
        String result = objectMapper.writeValueAsString(ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenResponse));
        response.getWriter().write(result);
    }

}
