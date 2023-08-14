package com.bbacks.bst.auth.service;

import com.bbacks.bst.auth.dto.TokenResponse;
import com.bbacks.bst.common.exception.NeedLoginException;
import com.bbacks.bst.common.exception.RefreshTokenException;
import com.bbacks.bst.common.exception.UserIdNotFoundException;
import com.bbacks.bst.common.utils.JwtService;
import com.bbacks.bst.user.domain.User;
import com.bbacks.bst.user.repository.UserRepository;
import com.bbacks.bst.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.RedirectException;
import org.hibernate.boot.jaxb.mapping.JaxbPersistenceUnitDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
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
    private final UserRepository userRepository;

    private String createRefreshTokenKey(){
        return UUID.randomUUID().toString();
    }

    private String saveRefreshTokenInRedis(String refreshToken, Long expiredMs) {
        String refreshKey = createRefreshTokenKey();

        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set(refreshKey, refreshToken, expiredMs, TimeUnit.SECONDS);

        return refreshKey;
    }

    public void checkUserIdInDB(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            log.error("UserId:{}에 해당하는 유저를 DB 에서 찾을 수 없습니다.", userId);
            throw new UserIdNotFoundException();
        }
    }

    public TokenResponse login(Long userId){
        /**
         * controller에서 들어온 요청대로 회원가입하고 인증 과정 거치기
         */

        Long currentTime = System.currentTimeMillis();
        String accessToken = JwtService.generateToken(userId, secretKey, currentTime, accessExpiredMs);
        String refreshToken = JwtService.generateToken(userId, secretKey, currentTime, refreshExpiredMs);

        String refreshKey = saveRefreshTokenInRedis(refreshToken, refreshExpiredMs);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshKey(refreshKey)
                .build();
    }

    public String refresh(String refreshKey) {
        /**
         * refresh token을 이용한 access token 재발급
         * refresh token도 만료되었다면 재로그인
         */
        try {
            ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
            String refreshToken = stringStringValueOperations.get(refreshKey);
            if (refreshToken.isBlank()) {
                log.error("refreshKey 에 해당하는 refreshToken 이 없습니다.");
                throw new NeedLoginException();
            }
            if (!JwtService.validateToken(secretKey, refreshToken)) {
                log.error("refreshToken validation 에러");
                throw new NeedLoginException();
            }
            Long userId = JwtService.getUserId(secretKey, refreshToken);

            checkUserIdInDB(userId);

            String accessToken = JwtService.generateToken(userId, secretKey, System.currentTimeMillis(), accessExpiredMs);
            log.info("새로 발급한 accessToken: {} for userId: {}", accessToken, userId);
            return accessToken;
        } catch (UserIdNotFoundException e) {
            // 회원가입으로 돌아가야 함
            throw e;
        } catch (Exception e) {
            // 로그인 다시 하기
            throw new NeedLoginException();
        }

    }
}
