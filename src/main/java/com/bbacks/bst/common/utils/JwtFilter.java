package com.bbacks.bst.common.utils;

import com.bbacks.bst.auth.service.AuthService;
import com.bbacks.bst.common.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final String secretKey;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try{
//            String token = getJwtFromRequest(request);
//            if (!StringUtils.hasText(token) || !JwtService.validateToken(secretKey, token)) {
//                throw new JwtException("JWT을 확인하세요.");
//            }
//            Long userId = JwtService.getUserId(secretKey, token);
//
//            // 권한 부여
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, List
//                    .of(new SimpleGrantedAuthority("USER")));
//
//            // 디테일을 넣어준다.
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            filterChain.doFilter(request, response);
//        }catch (JwtException e){
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//
//            String result = objectMapper.writeValueAsString(ApiResponseDto.error(HttpStatus.UNAUTHORIZED, e.getMessage()));
//
//            response.getWriter().write(result);
//        }
        String token = getJwtFromRequest(request);
        if(StringUtils.hasText(token) && JwtService.validateToken(secretKey, token)){
            Long userId = JwtService.getUserId(secretKey, token);

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, List
                    .of(new SimpleGrantedAuthority("USER")));

            // 디테일을 넣어준다.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.split(" ")[1];
        }
        return null;

    }
}
