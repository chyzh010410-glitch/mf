package com.mf.fertilizer.serviceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.fertilizer.constant.RedisKey;
import com.mf.fertilizer.entity.FertilizationRule;
import com.mf.fertilizer.entity.Fertilizer;
import com.mf.fertilizer.mapper.FertilizationRuleMapper;
import com.mf.fertilizer.service.FertilizationRuleService;
import com.mf.fertilizer.service.FertilizerService;
import com.mf.fertilizer.util.SeasonUtil;
import com.mf.fertilizer.vo.RecommendResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FertilizationRuleServiceImpl extends ServiceImpl<FertilizationRuleMapper, FertilizationRule>
        implements FertilizationRuleService {

    private final FertilizerService fertilizerService;
    private final StringRedisTemplate redisTemplate;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<RecommendResultVO> recommend(String species, Integer age, String season) {
        var actualSeason = StrUtil.isNotBlank(season) ? season : SeasonUtil.currentSeason();

        // 查缓存
        var cacheKey = RedisKey.RECOMMEND + species + ":" + age + ":" + actualSeason;
        var cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            try {
                return MAPPER.readValue(cached, new TypeReference<List<RecommendResultVO>>() {});
            } catch (Exception ignored) {
            }
        }

        // 查询匹配规则: 同树种 + 树龄在范围内 + 季节匹配(含all)
        var rules = lambdaQuery()
                .eq(FertilizationRule::getSpecies, species)
                .le(FertilizationRule::getAgeMin, age)
                .ge(FertilizationRule::getAgeMax, age)
                .and(w -> w.eq(FertilizationRule::getSeason, actualSeason)
                        .or().eq(FertilizationRule::getSeason, "all"))
                .orderByDesc(FertilizationRule::getPriority)
                .list();

        // 关联肥料信息
        var fertMap = fertilizerService.getCachedList().stream()
                .collect(java.util.stream.Collectors.toMap(Fertilizer::getId, f -> f));

        var result = new ArrayList<RecommendResultVO>();
        for (var rule : rules) {
            var fert = fertMap.get(rule.getFertilizerId());
            if (fert == null) continue;
            var vo = new RecommendResultVO();
            vo.setFertilizerId(fert.getId());
            vo.setFertilizerName(fert.getName());
            vo.setFertilizerType(fert.getType());
            vo.setNutrientContent(fert.getNutrientContent());
            vo.setRecommendAmount(rule.getRecommendAmount());
            vo.setUnitPrice(fert.getUnitPrice());
            vo.setMethod(rule.getMethod());
            vo.setRuleRemark(rule.getRemark());
            vo.setPriority(rule.getPriority());
            result.add(vo);
        }
        result.sort(Comparator.comparingInt(RecommendResultVO::getPriority).reversed());

        // 缓存结果
        try {
            redisTemplate.opsForValue().set(cacheKey, MAPPER.writeValueAsString(result),
                    RedisKey.RECOMMEND_CACHE_HOURS, TimeUnit.HOURS);
        } catch (Exception ignored) {
        }

        return result;
    }
}
