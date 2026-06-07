package com.mf.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.server.entity.FertilizationRule;
import com.mf.common.vo.RecommendResultVO;
import java.util.List;

public interface FertilizationRuleService extends IService<FertilizationRule> {
    List<RecommendResultVO> recommend(String species, Integer age, String season);
}
