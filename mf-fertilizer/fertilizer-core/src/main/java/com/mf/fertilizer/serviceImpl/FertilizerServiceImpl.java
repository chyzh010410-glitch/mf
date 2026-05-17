package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.fertilizer.constant.RedisKey;
import com.mf.fertilizer.entity.Fertilizer;
import com.mf.fertilizer.mapper.FertilizerMapper;
import com.mf.fertilizer.service.FertilizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FertilizerServiceImpl extends ServiceImpl<FertilizerMapper, Fertilizer> implements FertilizerService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<Fertilizer> getCachedList() {
        var key = RedisKey.FERTILIZER_LIST;
        var json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return MAPPER.readValue(json, new TypeReference<List<Fertilizer>>() {});
            } catch (Exception ignored) {
            }
        }
        var list = list();
        try {
            redisTemplate.opsForValue().set(key, MAPPER.writeValueAsString(list),
                    RedisKey.LIST_CACHE_MINUTES, TimeUnit.MINUTES);
        } catch (Exception ignored) {
        }
        return list;
    }

    @Override
    public void evictCache() {
        redisTemplate.delete(RedisKey.FERTILIZER_LIST);
    }
}
