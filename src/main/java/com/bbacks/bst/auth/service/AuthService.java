package com.bbacks.bst.auth.service;

import com.bbacks.bst.auth.dto.TokenResponse;
import com.bbacks.bst.common.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.jaxb.mapping.JaxbPersistenceUnitDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expire-ms}")
    private Long accessExpiredMs;

    @Value("${jwt.refresh.expire-ms}")
    private Long refreshExpiredMs;

    private final RedisTemplate redisTemplate;

    public TokenResponse login(Long userId){
        /**
         * controller에서 들어온 요청대로 회원가입하고 인증 과정 거치기
         */

        Long currentTime = System.currentTimeMillis();
        String accessToken = JwtService.generateToken(userId, secretKey, currentTime, accessExpiredMs);
        String refreshToken = JwtService.generateToken(userId, secretKey, currentTime, refreshExpiredMs);

//        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
//        stringStringValueOperations.set(refreshToken, "userId:"+String.valueOf(userId), Duration.ofMillis(currentTime + refreshExpiredMs*1000));

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
