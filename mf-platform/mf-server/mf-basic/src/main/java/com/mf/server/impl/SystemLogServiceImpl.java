package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.SystemLog;
import com.mf.server.mapper.SystemLogMapper;
import com.mf.server.service.SystemLogService;
import org.springframework.stereotype.Service;

@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {}
