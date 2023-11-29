package com.bbacks.bst.domain.auth.service;

import com.bbacks.bst.global.jwt.service.JwtService;
import com.bbacks.bst.domain.user.domain.User;
import com.bbacks.bst.global.exception.NeedLoginException;
import com.bbacks.bst.global.exception.UserIdNotFoundException;
// import com.bbacks.bst.global.utils.JwtService;
import com.bbacks.bst.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private String createRefreshTokenKey(){
        return UUID.randomUUID().toString();
    }

    public String saveRefreshTokenInRedis(String refreshToken) {
        String refreshKey = createRefreshTokenKey();

        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set(refreshKey, refreshToken, refreshTokenExpirationPeriod, TimeUnit.MILLISECONDS);

        return refreshKey;
    }

    //DB에 refreshKey 저장
    public void saveRefreshKey(String refreshKey, String userSocialId) {
        User user = userRepository.findByUserSocialId(userSocialId)
                .orElseThrow(UserIdNotFoundException::new);
        user.updateRefreshToken(refreshKey);
        userRepository.save(user);

    }

    public Long checkSocialIdAndGetUserId(String socialId){
        Optional<User> user = userRepository.findByUserSocialId(socialId);
        if(user.isEmpty()){
            log.error("socialId:{}에 해당하는 유저를 DB 에서 찾을 수 없습니다.", socialId);
            throw new UserIdNotFoundException();
        }
        return user.get().getUserId();
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
            if (!jwtService.validateToken(refreshToken)) {
                log.error("refreshToken validation 에러");
                throw new NeedLoginException();
            }
            String socialId = jwtService.getSocialId(refreshToken);
            Long userId = checkSocialIdAndGetUserId(socialId);
            String accessToken = jwtService.createAccessToken(socialId);

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

    public void logout(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(UserIdNotFoundException::new);
        String redisKey = user.getUserToken();
        //redis에 정보가 존재하면 삭제 -> 로그아웃
        if (redisTemplate.opsForValue().get(redisKey) != null) {
            redisTemplate.delete(redisKey); //토큰 삭제
        }
    }
}
