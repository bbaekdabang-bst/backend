package com.bbacks.bst.debates.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Dislike")
@Getter
public class Dislike {
    @Id
    private String id;
    private Long userId;
    private Long postId;
}
