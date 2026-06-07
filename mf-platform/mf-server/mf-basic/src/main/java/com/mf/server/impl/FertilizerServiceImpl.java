package com.mf.server.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mf.server.entity.Fertilizer;
import com.mf.server.mapper.FertilizerMapper;
import com.mf.server.service.FertilizerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FertilizerServiceImpl extends ServiceImpl<FertilizerMapper, Fertilizer> implements FertilizerService {
    @Override @Cacheable(value="fertilizerList",key="'all'")
    public List<Fertilizer> getCachedList() { return lambdaQuery().list(); }
    @Override @CacheEvict(value="fertilizerList",allEntries=true)
    public void evictCache() {}
}
