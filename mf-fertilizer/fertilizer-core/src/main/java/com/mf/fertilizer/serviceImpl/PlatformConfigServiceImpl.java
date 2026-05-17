package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.PlatformConfig;
import com.mf.fertilizer.mapper.PlatformConfigMapper;
import com.mf.fertilizer.service.PlatformConfigService;
import org.springframework.stereotype.Service;

@Service
public class PlatformConfigServiceImpl extends ServiceImpl<PlatformConfigMapper, PlatformConfig> implements PlatformConfigService {
}
