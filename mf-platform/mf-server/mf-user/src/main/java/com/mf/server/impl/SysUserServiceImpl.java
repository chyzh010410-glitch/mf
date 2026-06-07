package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.SysUser;
import com.mf.server.mapper.SysUserMapper;
import com.mf.server.service.SysUserService;
import org.springframework.stereotype.Service;

@Service public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {}
