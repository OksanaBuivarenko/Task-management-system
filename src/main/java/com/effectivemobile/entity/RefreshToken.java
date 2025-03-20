package com.effectivemobile.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@RedisHash("refresh_tokens")
@Data
@Builder
public class RefreshToken {
    @Id
    @Indexed
    private Long id;

    @Indexed
    private Long userId;

    @Indexed
    private String token;

    @Indexed
    private Instant expiredDate;
}
