package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.PointsRecord;
import com.mf.server.mapper.PointsRecordMapper;
import com.mf.server.service.PointsRecordService;
import org.springframework.stereotype.Service;

@Service
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements PointsRecordService {}
