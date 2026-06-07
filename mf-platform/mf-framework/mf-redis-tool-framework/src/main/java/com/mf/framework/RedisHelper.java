package com.mf.framework;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具 —— 统一 Key 前缀管理与常用操作封装。
 * 所有服务通过此工具操作 Redis，保证 Key 命名一致。
 */
@Component
@RequiredArgsConstructor
public class RedisHelper {

    private final StringRedisTemplate redis;

    // ========== String ==========
    public String get(String key) { return redis.opsForValue().get(key); }
    public void set(String key, String value, long timeout, TimeUnit unit) { redis.opsForValue().set(key, value, timeout, unit); }
    public void delete(String key) { redis.delete(key); }
    public boolean exists(String key) { return Boolean.TRUE.equals(redis.hasKey(key)); }

    // ========== 计数器 ==========
    public Long incr(String key) { return redis.opsForValue().increment(key); }
    public Long decr(String key) { return redis.opsForValue().decrement(key); }

    // ========== Token ==========
    public void saveToken(String prefix, String token, long days) {
        redis.opsForValue().set(prefix + token, "1", days, TimeUnit.DAYS);
    }
    public void removeToken(String prefix, String token) {
        redis.delete(prefix + token);
    }
    public boolean tokenExists(String prefix, String token) {
        return Boolean.TRUE.equals(redis.hasKey(prefix + token));
    }
}
