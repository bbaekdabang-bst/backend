package com.bbacks.bst.debates.domain;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Like")
@Getter
public class Like {
    @Id
    private String id;
    private Long userId;
    private long postId;
}
