package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.ActivityEntity;
import com.mf.server.mapper.ActivityEntityMapper;
import com.mf.server.service.ActivityEntityService;
import org.springframework.stereotype.Service;

@Service public class ActivityEntityServiceImpl extends ServiceImpl<ActivityEntityMapper, ActivityEntity> implements ActivityEntityService {}
