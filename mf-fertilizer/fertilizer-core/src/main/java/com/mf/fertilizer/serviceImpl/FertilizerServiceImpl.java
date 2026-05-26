package com.mf.fertilizer.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.fertilizer.entity.Fertilizer;
import com.mf.fertilizer.mapper.FertilizerMapper;
import com.mf.fertilizer.service.FertilizerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FertilizerServiceImpl extends ServiceImpl<FertilizerMapper, Fertilizer> implements FertilizerService {

    @Override
    @Cacheable(value = "fertilizerList", key = "'all'")
    public List<Fertilizer> getCachedList() {
        return list();
    }

    @Override
    @CacheEvict(value = "fertilizerList", allEntries = true)
    public void evictCache() {
    }
}
