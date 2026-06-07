package com.mf.framework;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 验证码服务 —— 支持手机/邮箱验证码生成与校验。
 * 6 位数字，5 分钟有效，Redis 存储。
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final StringRedisTemplate redis;

    /** 生成验证码并存入 Redis */
    public String generate(String target, String type) {
        String code = RandomUtil.randomNumbers(6);
        redis.opsForValue().set("captcha:" + type + ":" + target, code, 5, TimeUnit.MINUTES);
        return code;
    }

    /** 校验验证码，校验通过后删除 */
    public boolean verify(String target, String type, String code) {
        String key = "captcha:" + type + ":" + target;
        String cached = redis.opsForValue().get(key);
        if (cached != null && cached.equals(code)) {
            redis.delete(key);
            return true;
        }
        return false;
    }
}
