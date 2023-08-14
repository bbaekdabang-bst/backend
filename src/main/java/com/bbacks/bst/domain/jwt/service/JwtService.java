package com.bbacks.bst.domain.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
@PropertySource("classpath:application-jwt.properties")
public class JwtService {

    //application-jwt.properties 속성 값 주입
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;


    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    //jwt Subject&Claim으로 SocialId와 platformType 사용
    private static final String SOCIAL_ID_CLAIM = "socialId";
    private static final String PLATFORM_TYPE_CLAIM = "platformType";
    private static final String BEARER = "Bearer";

    private final UserRepository userRepository;


    /**
     * AccessToken 생성
     */
    public String createAccessToken(String socialId, String platformType){
        Date now = new Date();
        //AccessToken을 만듦, 만료시간 설정, Claim 설정, secret key로 암호화
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(SOCIAL_ID_CLAIM, socialId)
                .withClaim(PLATFORM_TYPE_CLAIM, platformType)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * 재발급 시 AccessToken 헤더에 실어서 전송
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken){
        response.setStatus(HttpServletResponse.SC_OK); //200 ok

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token: {}", accessToken);
    }

    /**
     * 로그인 시 AccessToken + RefreshToken 헤더에 실어서 전송
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK); //200 ok

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, RefreshToken 헤더 설정 완료");
    }

    //AccessToken 헤더 설정
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken){
        response.setHeader(accessHeader, accessToken);
    }

    //RefreshToken 헤더 설정
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken){
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * RefreshToken DB에 저장(업데이트)
     */
    public void updateRefreshToken(String socialId, String platformType, String refreshToken){
        userRepository.findByUserPlatformAndUserSocialId(PlatformType.valueOf(platformType), socialId)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    /**
     * Token 유효성 검증
     */
    public boolean isTokenValid(String token){
        try{
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch(Exception e){
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }


    //헤더에서 RefreshToken 추출: Bearer을 제외하고 순수 토큰만 가져옴
    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    //헤더에서 AccessToken 추출: Bearer을 제외하고 순수 토큰만 가져옴
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    //AccessToken에서 socialId 추출(추출 전 검증 진행)
    public Optional<String> extractSocialId(String accessToken){
        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(SOCIAL_ID_CLAIM)
                    .asString());
        } catch(Exception e){
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    //AccessToken에서 platformType 추출(추출 전 검증 진행)
    public Optional<String> extractPlatformType(String accessToken){
        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(PLATFORM_TYPE_CLAIM)
                    .asString());
        } catch(Exception e){
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }


}
