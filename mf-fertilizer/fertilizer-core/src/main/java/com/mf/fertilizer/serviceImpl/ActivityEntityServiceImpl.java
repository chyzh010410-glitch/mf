package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.ActivityEntity;
import com.mf.fertilizer.mapper.ActivityEntityMapper;
import com.mf.fertilizer.service.ActivityEntityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityEntityServiceImpl extends ServiceImpl<ActivityEntityMapper, ActivityEntity> implements ActivityEntityService {
}
