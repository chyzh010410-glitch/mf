package com.mf.fertilizer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.fertilizer.entity.FertilizationRecord;
import com.mf.fertilizer.vo.StatsVO;

public interface FertilizationRecordService extends IService<FertilizationRecord> {

    StatsVO getStats();
}
