package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.FertilizationRecord;
import java.util.Map;

public interface FertilizationRecordService extends IService<FertilizationRecord> {
    Map<String,Object> getStats();
}
