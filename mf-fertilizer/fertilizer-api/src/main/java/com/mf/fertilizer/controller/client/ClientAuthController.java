package com.mf.fertilizer.controller.client;

import com.mf.fertilizer.dto.client.*;
import com.mf.fertilizer.entity.User;
import com.mf.fertilizer.entity.VerificationCode;
import com.mf.fertilizer.service.UserService;
import com.mf.fertilizer.service.VerificationCodeService;
import com.mf.fertilizer.util.JwtUtil;
import com.mf.fertilizer.vo.ResultVO;
import com.mf.fertilizer.vo.client.UserLoginResultVO;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/client/auth")
@RequiredArgsConstructor
public class ClientAuthController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    private final StringRedisTemplate redisTemplate;
    private static final String CLIENT_TOKEN_PREFIX = "client:token:";
    private static final String CAPTCHA_PREFIX = "verify:code:";

    @PostMapping("/register")
    public ResultVO<?> register(@Valid @RequestBody UserRegisterDTO dto) {
        // verify code
        var codes = verificationCodeService.lambdaQuery()
                .eq(VerificationCode::getTarget, dto.getPhone())
                .eq(VerificationCode::getType, "register")
                .eq(VerificationCode::getUsed, 0)
                .ge(VerificationCode::getExpireTime, LocalDateTime.now())
                .orderByDesc(VerificationCode::getCreateTime)
                .list();
        if (codes.isEmpty() || !codes.get(0).getCode().equals(dto.getCode())) {
            return ResultVO.fail("验证码错误或已过期");
        }
        // mark code used
        var code = codes.get(0);
        code.setUsed(1);
        verificationCodeService.updateById(code);
        // check username duplicate
        if (userService.lambdaQuery().eq(User::getUsername, dto.getUsername()).count() > 0) {
            return ResultVO.fail("用户名已存在");
        }
        if (userService.lambdaQuery().eq(User::getPhone, dto.getPhone()).count() > 0) {
            return ResultVO.fail("手机号已注册");
        }
        var user = new User();
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(md5Password);
        user.setNickname("用户" + System.currentTimeMillis() % 100000);
        user.setStatus(1);
        user.setPoints(0);
        userService.save(user);
        return ResultVO.success();
    }

    @PostMapping("/login")
    public ResultVO<UserLoginResultVO> login(@Valid @RequestBody UserLoginDTO dto) {
        var user = userService.lambdaQuery()
                .eq(dto.getUsername() != null, User::getUsername, dto.getUsername())
                .eq(dto.getPhone() != null, User::getPhone, dto.getPhone())
                .one();
        if (user == null) return ResultVO.fail(401, "用户名或手机号不存在");
        if (user.getStatus() == 0) return ResultVO.fail(401, "账号已被禁用");
        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!md5Password.equals(user.getPassword())) return ResultVO.fail(401, "密码错误");
        String token = JwtUtil.generateWithUserType(user.getId(), user.getUsername(), "consumer", "consumer");
        redisTemplate.opsForValue().set(CLIENT_TOKEN_PREFIX + token, String.valueOf(user.getId()), 7, TimeUnit.DAYS);
        var vo = new UserLoginResultVO(token, user.getId(), user.getUsername(), user.getNickname(), user.getAvatar(), user.getPoints());
        return ResultVO.success(vo);
    }

    @PostMapping("/logout")
    public ResultVO<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            redisTemplate.delete(CLIENT_TOKEN_PREFIX + authHeader.substring(7));
        }
        return ResultVO.success();
    }

    @PostMapping("/captcha")
    public ResultVO<?> captcha(@RequestBody CaptchaRequest dto) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        var vc = new VerificationCode();
        vc.setTarget(dto.getTarget());
        vc.setCode(code);
        vc.setType(dto.getType());
        vc.setUsed(0);
        vc.setExpireTime(LocalDateTime.now().plusMinutes(5));
        vc.setCreateTime(LocalDateTime.now());
        verificationCodeService.save(vc);
        redisTemplate.opsForValue().set(CAPTCHA_PREFIX + dto.getTarget() + ":" + dto.getType(), code, 5, TimeUnit.MINUTES);
        // In production, send via SMS/email; for dev, return code
        return ResultVO.success(code);
    }

    @PostMapping("/reset-password")
    public ResultVO<?> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_PREFIX + dto.getPhone() + ":reset_password");
        if (cachedCode == null || !cachedCode.equals(dto.getCode())) return ResultVO.fail("验证码错误或已过期");
        var user = userService.lambdaQuery().eq(User::getPhone, dto.getPhone()).one();
        if (user == null) return ResultVO.fail("手机号未注册");
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getNewPassword().getBytes(StandardCharsets.UTF_8)));
        userService.updateById(user);
        return ResultVO.success();
    }

    @Data
    public static class CaptchaRequest {
        private String target;
        private String type;
    }
}
