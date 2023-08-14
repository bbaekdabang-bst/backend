package com.bbacks.bst.domain.jwt.filter;

import com.bbacks.bst.domain.jwt.service.JwtService;
import com.bbacks.bst.domain.user.domain.PlatformType;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * "/user/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 *
 *  1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리(일반적인 경우)
 *  2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR -> 재로그인
 *  3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, 인증 성공 처리는 하지 않고 실패 처리
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/user/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/user/login" 요청이 들어오면, 다음 필터 호출
            return; //현재 필터 진행 막음
        }

        //request에서 refreshToken 추출, 없는 경우 null 반환
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        //refreshToken이 존재한 경우, DB의 refreshToken과 일치하는지 확인 후 accessToken 재발급
        if(refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }
        //refreshToken이 없거나 유효하지 않다면, accessToken을 검사하고 인증을 처리하는 로직 수행
        //accessToken이 없거나 유효하지 않을 경우 에러 발생
        if(refreshToken == null){
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     * refreshToken으로 유저 찾기, AccessToken 재발급 메소드
     * 추출한 refreshToken으로 DB에서 유저를 찾고, 해당 유저가 있다면 AccessToken 생성
     * 이후 JwtService.sendAccessToken()으로 응답 헤더에 보냄
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken){
        userRepository.findByUserToken(refreshToken)
                .ifPresent(user ->
                        jwtService.sendAccessToken(response,
                                jwtService.createAccessToken(user.getUserSocialId(), user.getUserPlatform().toString())));
    }

    /**
     * accessToken 체크, 인증 처리 메소드
     * accessToken 추출 후 유효한 토큰인지 검증
     * accessToken으로부터 socialId와 platformType을 추출하여 해당 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담음
     * 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException{
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractSocialId(accessToken)
                        .ifPresent(socialId -> jwtService.extractPlatformType(accessToken)
                                .ifPresent(platformType ->
                                        userRepository.findByUserPlatformAndUserSocialId(PlatformType.valueOf(platformType), socialId)
                                                .ifPresent(this::saveAuthentication))));

        filterChain.doFilter(request, response);
    }


    /**
     * 인증 허가 메소드
     * UserDetails의 User을 builder로 생성 후 해당 객체 인증처리
     * 해당 객체를 SecurityContextHolder에 담음
     */
    public void saveAuthentication(User myUser){
        String password = null;
        String username = myUser.getUserPlatform().toString() + myUser.getUserSocialId();
        //username: platformType + socialId 으로 함
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .roles(myUser.getUserRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
