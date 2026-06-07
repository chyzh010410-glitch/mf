package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.PlatformConfig;
import com.mf.server.mapper.PlatformConfigMapper;
import com.mf.server.service.PlatformConfigService;
import org.springframework.stereotype.Service;

@Service
public class PlatformConfigServiceImpl extends ServiceImpl<PlatformConfigMapper, PlatformConfig> implements PlatformConfigService {}
