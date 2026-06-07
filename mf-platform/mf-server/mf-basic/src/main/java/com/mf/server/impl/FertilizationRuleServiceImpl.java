package com.mf.server.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.FertilizationRule;
import com.mf.server.entity.Fertilizer;
import com.mf.server.mapper.FertilizationRuleMapper;
import com.mf.server.service.FertilizationRuleService;
import com.mf.server.service.FertilizerService;
import com.mf.common.vo.RecommendResultVO;
import com.mf.common.util.SeasonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class FertilizationRuleServiceImpl extends ServiceImpl<FertilizationRuleMapper, FertilizationRule>
        implements FertilizationRuleService {
    private final FertilizerService fertilizerService;

    @Override
    public List<RecommendResultVO> recommend(String species, Integer age, String season) {
        return doRecommend(species, age, StrUtil.isNotBlank(season) ? season : SeasonUtil.currentSeason());
    }

    @Cacheable(value="recommend",key="#species+':'+#age+':'+#season")
    public List<RecommendResultVO> doRecommend(String species, Integer age, String season) {
        var rules = lambdaQuery().eq(FertilizationRule::getSpecies, species)
                .le(FertilizationRule::getAgeMin, age).ge(FertilizationRule::getAgeMax, age)
                .and(w->w.eq(FertilizationRule::getSeason,season).or().eq(FertilizationRule::getSeason,"all"))
                .orderByDesc(FertilizationRule::getPriority).list();
        var fertMap = fertilizerService.getCachedList().stream().collect(Collectors.toMap(Fertilizer::getId,f->f));
        var result = new ArrayList<RecommendResultVO>();
        for(var rule:rules){
            var fert = fertMap.get(rule.getFertilizerId()); if(fert==null) continue;
            var vo = new RecommendResultVO();
            vo.setFertilizerId(fert.getId()); vo.setFertilizerName(fert.getName());
            vo.setFertilizerType(fert.getType()); vo.setNutrientContent(fert.getNutrientContent());
            vo.setRecommendAmount(rule.getRecommendAmount()); vo.setUnitPrice(fert.getUnitPrice());
            vo.setMethod(rule.getMethod()); vo.setPriority(rule.getPriority());
            result.add(vo);
        }
        result.sort(Comparator.comparingInt(RecommendResultVO::getPriority).reversed());
        return result;
    }
}
