package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.common.util.JwtUtil;
import com.mf.server.entity.User;
import com.mf.server.mapper.UserMapper;
import com.mf.server.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final StringRedisTemplate redis;
    public UserServiceImpl(StringRedisTemplate redis) { this.redis = redis; }

    @Override
    public Map<String, Object> login(String username, String password) {
        var user = lambdaQuery().eq(User::getUsername, username).one();
        if (user == null || user.getStatus() == 0) throw new RuntimeException("用户不存在或已禁用");
        if (!DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)).equals(user.getPassword()))
            throw new RuntimeException("密码错误");
        String token = JwtUtil.generateWithUserType(user.getId(), user.getUsername(), "consumer", "consumer");
        redis.opsForValue().set("client:token:" + token, "1", 7, TimeUnit.DAYS);
        return Map.of("token", token, "userId", user.getId(), "username", user.getUsername(), "nickname", user.getNickname());
    }
}
