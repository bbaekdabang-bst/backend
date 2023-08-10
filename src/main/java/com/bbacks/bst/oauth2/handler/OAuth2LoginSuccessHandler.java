package com.bbacks.bst.oauth2.handler;

import com.bbacks.bst.jwt.service.JwtService;
import com.bbacks.bst.oauth2.CustomOAuth2User;
import com.bbacks.bst.user.domain.PlatformType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try{
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            PlatformType userPlatform = oAuth2User.getPlatformType(); //소셜 플랫폼(NAVER, KAKAO)
            String userSocialId = oAuth2User.getName(); //소셜 아이디

            //Token 생성
            String accessToken = jwtService.createAccessToken(userSocialId, userPlatform.toString());
            String refreshToken = jwtService.createRefreshToken();
            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
            response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

            //token 전송 & DB에 refreshToken 저장
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            jwtService.updateRefreshToken(userSocialId, userPlatform.toString(), refreshToken);

            } catch (Exception e) {
            throw e;
        }
    }

}
