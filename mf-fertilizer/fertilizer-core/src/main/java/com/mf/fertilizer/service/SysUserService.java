package com.mf.fertilizer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.fertilizer.entity.SysUser;
import com.mf.fertilizer.vo.LoginResultVO;

public interface SysUserService extends IService<SysUser> {

    LoginResultVO login(String username, String password);

    void logout(String token);

    SysUser getByUsername(String username);
}
