package com.bbacks.bst.domain.debates.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX_LIKE = "post:like:%d";
    private static final String PREFIX_DISLIKE = "post:dislike:%d";

    public boolean doLike(Long postId, Long userId){
        String likeKey = String.format(PREFIX_LIKE, postId);
        String dislikeKey = String.format(PREFIX_DISLIKE, postId);

        removeFromSet(dislikeKey, userId);

        if(isMemberInSet(likeKey, userId)){
            // 좋아요 취소
            removeFromSet(likeKey, userId);
            return false;
        }else{
            // 좋아요 추가
            addToSet(likeKey, userId);
            return true;
        }
    }

    public boolean doDisLike(Long postId, Long userId){
        String likeKey = String.format(PREFIX_LIKE, postId);
        String dislikeKey = String.format(PREFIX_DISLIKE, postId);

        removeFromSet(likeKey, userId);

        if(isMemberInSet(dislikeKey, userId)){
            // 싫어요 취소
            removeFromSet(dislikeKey, userId);
            return false;
        }else{
            addToSet(dislikeKey, userId);
            return true;
        }
    }

    public boolean isMemberInLikeSet(Long postId, Long userId){
        String likeKey = String.format(PREFIX_LIKE, postId);
        return isMemberInSet(likeKey, userId);
    }

    public boolean isMemberInDislikeSet(Long postId, Long userId){
        String dislikeKey = String.format(PREFIX_DISLIKE, postId);
        return isMemberInSet(dislikeKey, userId);
    }

    public Long getLikeCount(Long postId){
        String likeKey = String.format(PREFIX_LIKE, postId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    public Long getDislikeCount(Long postId){
        String dislikeKey = String.format(PREFIX_DISLIKE, postId);
        return redisTemplate.opsForSet().size(dislikeKey);
    }

    private boolean isMemberInSet(String key, Long userId){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    private void addToSet(String likeKey, Long userId){
        redisTemplate.opsForSet().add(likeKey, String.valueOf(userId));
    }
    private void removeFromSet(String likeKey, Long userId){
        redisTemplate.opsForSet().remove(likeKey, String.valueOf(userId));
    }


}
