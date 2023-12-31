package com.bbacks.bst.global.jwt.filter;

import com.bbacks.bst.domain.auth.service.AuthService;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.domain.user.repository.UserRepository;
import com.bbacks.bst.global.exception.UserIdNotFoundException;
import com.bbacks.bst.global.exception.UserNotFoundInRedisException;
import com.bbacks.bst.global.jwt.service.JwtService;
import com.bbacks.bst.global.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final JwtService jwtService;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);
            if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
                String socialId = jwtService.getSocialId(token);

                Long userId = authService.checkSocialIdAndGetUserId(socialId);

                //로그인된 유저가 맞는지 검증
                User user = userRepository.findByUserSocialId(socialId)
                        .orElseThrow(UserIdNotFoundException::new);
                String redisKey = user.getUserToken();
                if(!redisTemplate.hasKey(redisKey) || redisTemplate.opsForValue().get(redisKey) == null) {
                    throw new UserNotFoundInRedisException();
                }

                // 권한 부여
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, List
                        .of(new SimpleGrantedAuthority("USER")));

                // 디테일을 넣어준다.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleException(response, e.getMessage(), "expired");
        } catch (Exception e) {
            handleException(response, e.getMessage(), null);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.split(" ")[1];
        }
        return null;

    }

    private void handleException(HttpServletResponse response, String message, String flag) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String result;
        if (flag != null) {
            result = objectMapper.writeValueAsString(ApiResponseDto.error(HttpStatus.UNAUTHORIZED, message, "expired"));
        } else {
            result = objectMapper.writeValueAsString(ApiResponseDto.error(HttpStatus.UNAUTHORIZED, message));
        }

        response.getWriter().write(result);
    }
}

