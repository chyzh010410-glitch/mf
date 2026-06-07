package com.mf.server.controller;
import cn.hutool.crypto.digest.BCrypt;
import com.mf.common.base.ResultVO;
import com.mf.common.context.UserContext;
import com.mf.common.constant.RedisKey;
import com.mf.common.util.JwtUtil;
import com.mf.server.entity.User;
import com.mf.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController @RequestMapping("/client/auth") @RequiredArgsConstructor
public class ClientAuthController {
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/register")
    public ResultVO<?> register(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String phone = body.get("phone");
        if (username == null || password == null) return ResultVO.fail(400, "用户名和密码不能为空");
        if (userService.lambdaQuery().eq(User::getUsername, username).count() > 0)
            return ResultVO.fail(400, "用户名已存在");
        var user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        user.setPhone(phone); user.setNickname(username); user.setStatus(1);
        userService.save(user);
        return ResultVO.success();
    }

    @PostMapping("/login")
    public ResultVO<?> login(@RequestBody Map<String,String> body) {
        try {
            return ResultVO.success(userService.login(body.get("username"), body.get("password")));
        } catch (RuntimeException e) {
            return ResultVO.fail(400, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResultVO<?> logout(@RequestHeader("Authorization") String auth) {
        if (auth != null && auth.startsWith("Bearer "))
            redisTemplate.delete("client:token:" + auth.substring(7));
        return ResultVO.success();
    }
}
