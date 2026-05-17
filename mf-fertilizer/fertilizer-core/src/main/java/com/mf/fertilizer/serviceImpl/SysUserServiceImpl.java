package com.mf.fertilizer.serviceImpl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.constant.RedisKey;
import com.mf.fertilizer.constant.ResultCode;
import com.mf.fertilizer.entity.SysUser;
import com.mf.fertilizer.exception.BusinessException;
import com.mf.fertilizer.mapper.SysUserMapper;
import com.mf.fertilizer.service.SysUserService;
import com.mf.fertilizer.util.JwtUtil;
import com.mf.fertilizer.vo.LoginResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginResultVO login(String username, String password) {
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或密码不能为空");
        }
        var user = getByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用");
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        var token = JwtUtil.generate(user.getId(), user.getUsername(), user.getRole());
        redisTemplate.opsForValue().set(
                RedisKey.LOGIN_TOKEN + token, "1",
                RedisKey.TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);

        var result = new LoginResultVO();
        result.setToken(token);
        result.setUsername(user.getUsername());
        result.setRealName(user.getRealName());
        result.setRole(user.getRole());
        return result;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisKey.LOGIN_TOKEN + token);
    }

    @Override
    public SysUser getByUsername(String username) {
        return lambdaQuery().eq(SysUser::getUsername, username).one();
    }
}
