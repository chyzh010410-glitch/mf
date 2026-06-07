package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.FertilizationRecord;
import com.mf.server.mapper.FertilizationRecordMapper;
import com.mf.server.service.FertilizationRecordService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class FertilizationRecordServiceImpl extends ServiceImpl<FertilizationRecordMapper, FertilizationRecord>
        implements FertilizationRecordService {
    @Override
    public Map<String,Object> getStats() {
        long total = count();
        return Map.of("totalRecords", total);
    }
}
