package com.mf.fertilizer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mf.fertilizer.entity.FertilizationRule;
import com.mf.fertilizer.vo.RecommendResultVO;

import java.util.List;

public interface FertilizationRuleService extends IService<FertilizationRule> {

    List<RecommendResultVO> recommend(String species, Integer age, String season);
}
